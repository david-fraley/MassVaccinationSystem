// Tests broker/services/PatientIdService.js.
//
// Pre-requisites:
// 1) Broker DB is running (use broker_db container)
// 2) .env file contains DB_USERNAME and DB_PASSWORD settings set to same values as
//    POSTGRES_USER and POSTGRES_PASSWORD used for broker_db, e.g.,
//    DB_USERNAME=massvaxxadmin
//    DB_PASSWORD=massvaxxadmin
// 3) "npm install" has been run in broker directory.
//
// Run tests:
//    node PatietIdServiceUnitTest.js
//

const PatientIdService = require('../PatientIdService');

async function main() {

    var numFail = 0;
    var numPass = 0;
    var patient_id = '1';
    var qr_code = '0000-0000-0000-0000';

    console.log('Testing createPatientId()');
    var pid = await PatientIdService.createPatientId();
    console.log(pid);
    if ((pid == null) || (pid.patient_id == null))
    {
        console.log('FAIL');
        numFail++;
    }
    else
    {
        console.log('PASS');
        numPass++
        patient_id = pid.patient_id
    }

    console.log('Testing getPatientId('+patient_id+')');
    pid = await PatientIdService.getPatientId(patient_id);
    console.log(pid);
    if ((pid == null) || (pid.patient_id == null))
    {
        console.log('FAIL');
        numFail++;
    }
    else
    {
        console.log('PASS');
        numPass++
    }

    const pad_patient_id = '000000' + patient_id;
    console.log('Testing getPatientId('+pad_patient_id+')');
    pid = await PatientIdService.getPatientId(pad_patient_id);
    console.log(pid);
    if ((pid == null) || (pid.patient_id == null))
    {
        console.log('FAIL');
        numFail++;
    }
    else
    {
        console.log('PASS');
        numPass++
    }

    const bad_patient_id = '999999999999';
    console.log('Testing getPatientId('+bad_patient_id+')');
    pid = await PatientIdService.getPatientId(bad_patient_id);
    console.log(pid);
    if ((pid == null) || (pid.patient_id == null))
    {
        console.log('PASS');
        numPass++
    }
    else
    {
        console.log('FAIL');
        numFail++;
    }

    console.log('Testing createQrCodeForPatientId('+patient_id+')');
    var qrc = await PatientIdService.createQrCodeForPatientId(patient_id);
    console.log(qrc);
    if ((qrc == null) || (qrc.qr_code == null))
    {
        console.log('FAIL');
        numFail++;
    }
    else
    {
        console.log('PASS');
        numPass++
        qr_code = qrc.qr_code;
    }

    console.log('Testing createQrCodeForPatientId('+bad_patient_id+')');
    qrc = await PatientIdService.createQrCodeForPatientId(bad_patient_id);
    console.log(qrc);
    if ((qrc == null) || (qrc.qr_code == null))
    {
        console.log('PASS');
        numPass++;
    }
    else
    {
        console.log('FAIL');
        numFail++
    }

    console.log('Testing getPatientIdForQrCode('+qr_code+')');
    pid = await PatientIdService.getPatientIdForQrCode(qr_code);
    console.log(pid);
    if ((pid == null) || (pid.patient_id == null))
    {
        console.log('FAIL');
        numFail++;
    }
    else
    {
        console.log('PASS');
        numPass++
    }

    console.log('RESULTS: PASS='+numPass+' FAIL='+numFail);
    return;
}

main().then(console.log()).catch(console.error);