from flask import Flask, request, render_template,Response
from openai import OpenAI
import os
from dotenv import load_dotenv
from faq import process_question  # faq.py에서 process_question 함수 import
from orderhistory_database import get_order_history, get_order
from jinja2 import Template
from sqlalchemy import text
from database import get_db
from refund import get_refundable_orders, get_refundable_order_history
from product import get_product_search, search_product

load_dotenv()

app = Flask(__name__)

api_key = os.getenv("OPENAI_API_KEY")
client = OpenAI(api_key=api_key)

messages = []

# OpenAI API를 사용해 대화 응답을 생성하는 함수
def make_prompt(user_input):
    res = client.chat.completions.create(
        model="gpt-4o-mini",
        messages=user_input,
    )
    
    return res.choices[0].message.content

@app.route('/chat', methods=['GET','POST'])
def chat():
    if request.method == "POST":
        user_info = request.form.get('userInfo')
        message = request.form.get('message')

        if "구매내역" in message or "주문내역" in message:
            if not is_user(user_info):
                html_template = """
                <div class="message assistant">
                <strong>Assistant:</strong>
                <p>로그인 후 내역을 확인할 수 있습니다.</p>
                </div>"""
                template = Template(html_template)
                rendered_html = template.render()
                return Response(rendered_html, content_type="text/plain; charset=utf-8")
            return Response(get_order(user_info), content_type="text/plain; charset=utf-8")
        else:
            response_message = process_question(user_info, message)
            return Response(response_message, content_type="text/plain; charset=utf-8")

    return render_template("index.html", messages=messages)

@app.route('/order-history', methods=['GET'])
def order_history():
    user_id = request.args.get('userId')
    order_id = request.args.get('orderId')
    if not is_user(user_id):
        html_template = """
        <div class="message assistant">
        <strong>Assistant:</strong>
        <p>로그인 후 내역을 확인할 수 있습니다.</p>
        </div>"""
        template = Template(html_template)
        rendered_html = template.render()
        return Response(rendered_html, content_type="text/plain; charset=utf-8")
    return Response(get_order_history(user_id, order_id), content_type="text/plain; charset=utf-8")

@app.route('/refund', methods=['GET'])
def refund():
    user_id = request.args.get('userId')
    order_id = request.args.get('orderId')
    if not is_user(user_id):
        html_template = """
        <div class="message assistant">
        <strong>Assistant:</strong>
        <p>로그인 후 내역을 확인할 수 있습니다.</p>
        </div>"""
        template = Template(html_template)
        rendered_html = template.render()
        return Response(rendered_html, content_type="text/plain; charset=utf-8")
    if not order_id:
        return Response(get_refundable_orders(user_id), content_type="text/plain; charset=utf-8")
    else:
        return Response(get_refundable_order_history(user_id, order_id), content_type="text/plain; charset=utf-8")
    
@app.route('/product', methods=['GET'])
def product():
    product_id = request.args.get('productId')
    if not product_id:
        return Response(get_product_search(), content_type="text/plain; charset=utf-8")    
    return Response(search_product(product_id), content_type="text/plain; charset=utf-8")    
    
def is_user(user_info):
    db = next(get_db())
    query = text("SELECT count(*) FROM User WHERE user_id = :user_id")
    user = db.execute(query, {"user_id": user_info}).fetchone()
    
    return user[0]


# 0.0.0.0 으로 모든 IP에 대한 연결을 허용해놓고 포트는 8082로 설정
if __name__ == '__main__':
    app.run('0.0.0.0', port=8082, debug=True)