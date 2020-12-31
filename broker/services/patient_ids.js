// Patient ID service
//
const db = require('./database');
const PatientId = db.patient_ids;
const QrCode = db.qr_codes;

class PatientIdService {

    // Returns new patient ID record
    //
    async createPatientId() {
        const patientIdRecord = await PatientId.create({
            assigner: 'massvaxx'
        });
        return patientIdRecord;
    }

    // Returns entire patient ID record corresponding to provided patient ID value.
    //
    async getPatientId(patientId) {
        const patientIdRecord = await PatientId.findOne({
            where: {
                patient_id: patientId
            }
        });
        return patientIdRecord;
    }

    // Returns patient ID record corresponding to provided QR code.
    //
    async getPatientIdForQrCode(qrCode) {
        // Change to one query, e.g. "select patient_ids.* from patient_ids p, qr_codes q where p.patient_id=q.patient_id and q.qr_code=qrCode"
        //
        const qrCodeRecord = await QrCode.findOne({
            where: {
                qr_code: qrCode
            }
        });
        if (qrCodeRecord)
        {
            const patientIdRecord = await PatientId.findOne({
                where: {
                    patient_id: qrCodeRecord.patient_id
                }
            });
            return patientIdRecord;
        }
        else
        {
            return {};
        }
    }
}

module.exports = new PatientIdService();