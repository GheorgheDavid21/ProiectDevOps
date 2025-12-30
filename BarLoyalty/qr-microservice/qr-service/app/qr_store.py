import uuid

QR_CODES = {}

def generate_qr(transaction_id: str) -> str:
    qr_code = str(uuid.uuid4())
    QR_CODES[qr_code] = {
        "transaction_id": transaction_id,
        "valid": True
    }
    return qr_code

def validate_qr(qr_code: str) -> bool:
    data = QR_CODES.get(qr_code)
    if not data or not data["valid"]:
        return False

    data["valid"] = False
    return True