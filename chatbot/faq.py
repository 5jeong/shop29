import json
from jinja2 import Template
from langchain_openai import ChatOpenAI
import os

# OpenAI API 키 설정
api_key = os.getenv("OPENAI_API_KEY")
model_name = os.getenv("OPENAI_MODEL_NAME")
chat_model = ChatOpenAI(
    api_key=api_key,
    model = model_name,
    temperature = 0.7,
    max_tokens=300,
    max_retries=2,
)

# JSON 파일에서 FAQ 데이터 로드
with open('faq_data.json', 'r', encoding='utf-8') as f:
    faq_data = json.load(f)

# GPT-4 응답 생성 함수
def generate_response_with_gpt4(prompt):
    messages = [
        (
            "assistant",
            f"당신은 회사의 FAQ 데이터베이스에 대한 전문 지식을 가진 고객 서비스 전문가입니다. 해당 질문을 {faq_data}의 title과 가장 유사한 질문 하나만 찾아 해당 contents로 대답해주세요. 해당 질문이 데이터에 존재하지 않으면 다시 질문을 요청해주세요.응답 구성 방식은 알려주지 마세요. 사용자가 읽기 쉽게 줄바꿈 처리도 해주세요.",
        ),
        ("human", prompt),
    ]
    return chat_model.invoke(messages).content

# 질문에 대한 답변 처리 함수
def process_question(user_info, user_input):
    response = generate_response_with_gpt4(user_input)
    html_template = """
    <div class="message assistant">
    <strong>Assistant:</strong>
    <p>{{response}}</p>
    </div>"""
    template = Template(html_template)
    rendered_html = template.render(response=response)
    
    return rendered_html