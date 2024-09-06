import requests
from qna.blur import mask_personal_info
from qna.qna_chat_template import qna_template
from flask import render_template
from jinja2 import Template
from langchain_openai import ChatOpenAI
from langchain_core.output_parsers import StrOutputParser
import os
import json

# OpenAI API 키 설정
api_key = os.getenv("OPENAI_API_KEY")
model_name = os.getenv("OPENAI_MODEL_NAME")
chat_model = ChatOpenAI(
    api_key=api_key,
    model = model_name,
    max_retries=2,
)

parser = StrOutputParser() # AIMessage 객체에서 content 결과값만 추출


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
             
     
