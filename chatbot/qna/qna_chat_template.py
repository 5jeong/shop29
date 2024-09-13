qna_template = (
    """
    사용자의 입력을 분석하여, 조건에 맞는 문의 데이터를 응답.
    문의내역은 'createdTime'을 기준으로 내림차순 정렬.
    마크다운 문법은 사용하지 말고, json 형태로 답변할 것. 
    반드시 아래 답변 예시와 동일한 형식으로 답변.
    문의내역과 관련되지 않은 답변이라고 판단된다면, "canAnswer" 필드의 값을 false로 지정.
    문의내역과 관련된 답변이라고 판단된다면, "canAnswer" 필드의 값을 true로 지정.
    ---
    답변 예시 1
    질문 : 최근 문의내역 3건 알려줘
    답변 : 
    {{
        "answer" : "최근 문의내역 3건을 알려드리겠습니다.",
        "qnaList" : [
            {{
                "qnaTypeName": "매장문의",
                "qnaId": 1765,
                "title": "안녕하세요",
                "content": "안녕하세요",
                "createdTime": "2024-09-04T20:37:05",
                "isAnswerd" : true,
            }},
            {{
                "qnaTypeName": "매장문의",
                "qnaId": 1765,
                "title": "안녕하세요",
                "content": "안녕하세요",
                "createdTime": "2024-09-04T20:37:05",
                "isAnswerd" : false
            }},
            {{
                "qnaTypeName": "매장문의",
                "qnaId": 1765,
                "title": "안녕하세요",
                "content": "안녕하세요",
                "createdTime": "2024-09-04T20:37:05",
                "isAnswerd" : false
            }},
        ],
        "canAnswer": true
    }}
    ---
    답변 예시 2
    질문 : 2024년 1월에 문의했던 내용 보여줘
    답변
    {{
        "answer" : "해당 시기에 등록한 문의내역은 존재하지 않습니다.",
        "qnaList" :[],
        "canAnswer": true
    }}
    ---
    답변 예시 3
    질문 : 안녕하세요!
    답변
    {{
        "answer" : "안녕하세요! 1:1문의와 관련된 질문해주세요~",
        "qnaList" :[],
        "canAnswer": true
    }}
    ---
    답변 예시 4
    질문 : 제가 그동안 작성한 문의내역의 문의유형들은 무엇인가요?
    답변
    {{
        "answer" : "회원님께서 작성한 문의내역의 문의유형들은 '매장문의','기타문의'입니다.",
        "qnaList" :[],
        "canAnswer": true
    }}
    ---
    답변 예시 5
    질문 : 첨부파일을 이미지로 보여줘
    답변
    {{
        "answer" : "해당 내용은 답변드리기 어렵습니다. 상담원 연결시 도움드리겠습니다.",
        "qnaList" :[],
        "canAnswer": false
    }}
    ---
    1:1문의 데이터 : {qna_list}
    사용자 입력 : {user_input}
    """
)