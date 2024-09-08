import re

def mask_personal_info(text):
    """
    입력된 텍스트에서 이메일, 전화번호, 신용카드 번호 등을 완전히 마스킹 처리하는 함수
    """

    if not text:
        return text

    # 이메일 주소 전체 마스킹
    email_pattern = r'[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}'
    email_masking_text = re.sub(email_pattern, '*****', text)
    
    # 전화번호 전체 마스킹 (010-1234-5678 -> ***-****-****)
    phone_pattern = r'\b\d{2,3}[-.\s]?\d{3,4}[-.\s]?\d{4}\b'
    phone_masking_text = re.sub(phone_pattern, '***-****-****', email_masking_text)

    # 신용카드 번호 전체 마스킹 (1234-5678-1234-5678 -> ****-****-****-****)
    card_pattern = r'\b\d{4}[-.\s]?\d{4}[-.\s]?\d{4}[-.\s]?\d{4}\b'
    card_masking_text = re.sub(card_pattern, '****-****-****-****', phone_masking_text)

    return card_masking_text

if __name__ == "__main__":
    # 테스트용 텍스트
    email_phone_card_text = """
    이름: John Doe
    이메일: john.doe@example.com
    전화번호: 010-1234-5678
    신용카드 번호: 1234-5678-1234-5678
    """

    # 개인정보 마스킹 처리
    masked_text = mask_personal_info(email_phone_card_text)

    # 결과 출력
    print("Before Masking:\n", email_phone_card_text)
    print("After Masking:\n", masked_text)