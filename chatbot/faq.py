from crewai import Crew, Agent, Task
import json
from openai import OpenAI
from jinja2 import Template
import os

# OpenAI API 키 설정
api_key = os.getenv("OPENAI_API_KEY")
client = OpenAI(api_key=api_key)

# JSON 파일에서 FAQ 데이터 로드
with open('faq_data.json', 'r', encoding='utf-8') as f:
    faq_data = json.load(f)

# GPT-4 응답 생성 함수
def generate_response_with_gpt4(prompt):
    response = client.chat.completions.create(
        model="gpt-4o-mini",  # 최신 API에서 gpt-4 모델 지정
        messages=[{"role": "user", "content": prompt}],
        temperature=0.7,
        max_tokens=300,
    )
    return response.choices[0].message.content

# 래핑된 LLM 클래스 정의
class WrappedLLM:
    def __init__(self, llm_func):
        self.llm_func = llm_func

    def bind(self, **kwargs):
        return self.llm_func

# 래핑된 LLM 객체 생성
wrapped_llm = WrappedLLM(generate_response_with_gpt4)

# CrewAI Agent 생성
faq_agent = Agent(
    role="고객 서비스 어시스턴트",
    goal="사용자의 질문에 대해 FAQ 데이터를 기반으로 정확하고 도움이 되는 답변을 제공합니다.",
    backstory="당신은 회사의 FAQ 데이터베이스에 대한 전문 지식을 가진 고객 서비스 전문가입니다.",
    llm=wrapped_llm,
    verbose=True
)

# 질문에 대한 답변 처리 함수
def process_question(user_info, user_input):
    prompt = f"사용자가 다음과 같은 질문을 했습니다: '{user_input}'. 무의미한 질문은 질문을 다시 요청해주세요. 해당 질문을 {faq_data}의 title과 가장 유사한 질문 하나만 찾아 해당 contents로 대답해주세요. 해당 질문이 데이터에 존재하지 않으면 다시 질문을 요청해주세요.응답 구성 방식은 알려주지 마세요. 사용자가 읽기 쉽게 줄바꿈 처리도 해주세요. "
    
    response = faq_agent.llm.bind()(prompt)
    html_template = """
    <div class="message assistant">
    <strong>Assistant:</strong>
    <p>{{response}}</p>
    </div>"""
    template = Template(html_template)
    rendered_html = template.render(response=response)
    
    return rendered_html