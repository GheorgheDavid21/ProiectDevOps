from fastapi import FastAPI, HTTPException
from .models import GenerateQrRequest, GenerateQrResponse, ValidateQrRequest
from .qr_store import generate_qr, validate_qr

app = FastAPI(title="QR Service")

@app.get("/health")
def health():
    return {"status": "UP"}

@app.post("/qr/generate", response_model=GenerateQrResponse)
def generate(request: GenerateQrRequest):
    qr_code = generate_qr(request.transaction_id)
    return GenerateQrResponse(qr_code=qr_code)

@app.post("/qr/validate")
def validate(request: ValidateQrRequest):
    if not validate_qr(request.qr_code):
        raise HTTPException(status_code=400, detail="Invalid QR code")
    return {"status": "VALIDATED"}