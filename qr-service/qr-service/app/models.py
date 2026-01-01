from pydantic import BaseModel

class GenerateQrRequest(BaseModel):
    transaction_id: str

class GenerateQrResponse(BaseModel):
    qr_code: str

class ValidateQrRequest(BaseModel):
    qr_code: str