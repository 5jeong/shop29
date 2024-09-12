import requests
from qna.blur import mask_personal_info
from qna.qna_chat_template import qna_template
from flask import render_template
from jinja2 import Template
from langchain_openai import ChatOpenAI
from langchain_core.output_parsers import StrOutputParser
from langchain.prompts.chat import ChatPromptTemplate
import os
import json
from qna.vector_db.db_util import query_db

# OpenAI API 키 설정
api_key = os.getenv("OPENAI_API_KEY")
model_name = os.getenv("OPENAI_MODEL_NAME")
chat_model = ChatOpenAI(
    api_key=api_key,
    model = model_name,
    temperature=0.4,
    max_retries=2,
)

parser = StrOutputParser() # AIMessage 객체에서 content 결과값만 추출

CUR_DIR = os.path.dirname(os.path.abspath(__file__))

QNA_CREATE_ANSWER_TEMPLATE = os.path.join(
    CUR_DIR, "qna_create_answer_template.txt"
)


def get_qna_search(user_id: str, message: str) -> dict:
    """
    return {"chat_answer":"<html>~</html>","can_answer":True}
    """

    url = f'http://localhost:8080/qna/qna-list/{user_id}'
    response = requests.get(url)

    chat_answer = ""
    can_answer = False

    if response.status_code != 200:
        answer = '서버에 문제가 발생했습니다.'
    else:
        try:
            qna_list = response.json()

            for item in qna_list:
                item['title'] = mask_personal_info(item.get('title'))
                item['content'] = mask_personal_info(item.get('content'))
                item['answerContent'] = mask_personal_info(item.get('answerContent'))

            user_content = qna_template.format(qna_list = qna_list,user_input = message)

            prompt_ans = chat_model.invoke([("human",user_content)]).content

            ans_dict = json.loads(prompt_ans)

            answer = ans_dict['answer']
            selected_qna_list = ans_dict['qnaList']
            can_answer = ans_dict['canAnswer']

            chat_answer = render_template('qna_ans.html',answer=answer,qna_list=selected_qna_list)
        except Exception as e:
            return {"chat_answer":"","can_answer":can_answer}

    return {"chat_answer":chat_answer,"can_answer":can_answer}

def create_qna_answer(user_qna: str):
    # Vector DB에서 관련 FAQ 문서 추출
    related_documents = query_db(user_qna)

    # openai 에게 사용자 문의와 관련 문서들을 전달
    prompt_template_txt = _read_prompt_template(QNA_CREATE_ANSWER_TEMPLATE)
    prompt_template = ChatPromptTemplate.from_template(template=prompt_template_txt)

    chain = prompt_template | chat_model | parser
    answer = chain.invoke({"user_qna":user_qna,"related_documents":related_documents})

    # HTML 화면 렌더링하여 반환
    return render_template('qna_auto_ans.html',answer=answer,related_documents=related_documents)

def _read_prompt_template(file_path: str) -> str:
    with open(file_path, "r", encoding='utf-8') as f:
        prompt_template = f.read()
    return prompt_template