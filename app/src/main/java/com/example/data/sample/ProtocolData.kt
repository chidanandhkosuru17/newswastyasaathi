package com.example.data.sample

import com.example.data.local.ProtocolEntity

object ProtocolData {

    val allProtocols: List<ProtocolEntity> = listOf(
        // ANC - Antenatal Care
        ProtocolEntity(
            sectionId = "ANC-1.1",
            protocolCode = "ANC",
            protocolTitle = "Antenatal Care",
            title = "ANC Registration and Checkup Schedule",
            content = "Every pregnant woman must receive at least 4 mandatory ANC checkups: ANC-1 within the first 12 weeks of pregnancy; ANC-2 between 14 and 26 weeks; ANC-3 between 28 and 34 weeks; ANC-4 between 36 weeks and delivery. At each visit, measure weight, blood pressure, abdominal examination, and test urine for albumin/sugar.",
            keyFacts = "4 mandatory visits,ANC-1 within 12 weeks,ANC-2 at 14-26 weeks,ANC-3 at 28-34 weeks,ANC-4 at 36+ weeks",
            keywords = "ANC,antenatal,pregnancy checkup,registration,weeks,schedule,blood pressure,weight,trimester,anc kab hota hai"
        ),
        ProtocolEntity(
            sectionId = "ANC-1.2",
            protocolCode = "ANC",
            protocolTitle = "Antenatal Care",
            title = "Nutritional Support, IFA & Deworming",
            content = "Provide 180 Iron Folic Acid (IFA) tablets (one tablet daily containing 60mg elemental iron and 500mcg folic acid) starting from the second trimester (after 12 weeks). Provide 360 Calcium tablets (500mg elemental calcium twice daily, never taken together with IFA). Administer one dose of Albendazole (400mg) in the second trimester after 14 weeks for deworming.",
            keyFacts = "180 IFA tablets from 2nd trimester,360 Calcium tablets,Albendazole 400mg single dose in 2nd trimester,Do not take IFA and Calcium together",
            keywords = "IFA,iron,folic acid,calcium,albendazole,deworming,nutrition,tablets,dost,khana"
        ),
        ProtocolEntity(
            sectionId = "ANC-1.3",
            protocolCode = "ANC",
            protocolTitle = "Antenatal Care",
            title = "Td Vaccination during Pregnancy",
            content = "Administer Td-1 as early as pregnancy is confirmed. Administer Td-2 at least 4 weeks after Td-1. If the woman received 2 Td doses in a pregnancy within the last 3 years, only 1 Td-Booster dose is required during the current pregnancy.",
            keyFacts = "Td-1 early in pregnancy,Td-2 four weeks after Td-1,Td-Booster if 2 doses received within last 3 years",
            keywords = "Td,tetanus,vaccine,injection,pregnancy vaccination,Td1,Td2,booster,tika"
        ),
        ProtocolEntity(
            sectionId = "ANC-1.4",
            protocolCode = "ANC",
            protocolTitle = "Antenatal Care",
            title = "High-Risk Pregnancy (HRP) Identification",
            content = "Classify as High-Risk Pregnancy if: severe anaemia (Hb < 7 g/dL), high blood pressure (BP >= 140/90 mmHg), maternal age < 18 or > 35 years, height < 145 cm, history of previous caesarean section or stillbirth, multiple gestation (twins), gestational diabetes, or systemic medical illness. Immediate referral to Medical Officer / CHC is mandatory.",
            keyFacts = "Hb < 7 g/dL severe anaemia,BP >= 140/90 mmHg,Age <18 or >35,Previous C-section,Mandatory referral to MO",
            keywords = "high risk pregnancy,HRP,severe anaemia,high blood pressure,hypertension,twins,c-section,khatra,joshi"
        ),

        // IMM - Immunisation Schedules
        ProtocolEntity(
            sectionId = "IMM-1.1",
            protocolCode = "IMM",
            protocolTitle = "National Immunisation Schedule",
            title = "Birth Doses (within 24 Hours)",
            content = "At birth (or as early as possible within 24 hours): BCG 0.05 ml intradermal on left upper arm; Oral Polio Vaccine (OPV-0) 2 drops orally; Hepatitis B birth dose 0.5 ml intramuscular on anterolateral mid-thigh within 24 hours of birth.",
            keyFacts = "BCG 0.05 ml intradermal,OPV-0 2 drops,Hepatitis B within 24 hours 0.5 ml IM",
            keywords = "birth doses,BCG,OPV,Hepatitis B,Hep B,janm ke samay,newborn vaccines"
        ),
        ProtocolEntity(
            sectionId = "IMM-1.2",
            protocolCode = "IMM",
            protocolTitle = "National Immunisation Schedule",
            title = "Primary Doses at 6, 10, and 14 Weeks",
            content = "At 6 weeks: Pentavalent-1 (0.5ml IM), OPV-1 (2 drops), Rotavirus-1 (5 drops/dose), fIPV-1 (0.1ml intradermal), PCV-1 (0.5ml IM). At 10 weeks: Pentavalent-2, OPV-2, Rotavirus-2. At 14 weeks: Pentavalent-3, OPV-3, Rotavirus-3, fIPV-2, PCV-2. Minimum gap between doses is 4 weeks.",
            keyFacts = "6 weeks: Penta-1 OPV-1 RVV-1 fIPV-1 PCV-1,10 weeks: Penta-2 OPV-2 RVV-2,14 weeks: Penta-3 OPV-3 RVV-3 fIPV-2 PCV-2,Minimum 4 weeks gap",
            keywords = "6 weeks,10 weeks,14 weeks,pentavalent,rotavirus,fIPV,PCV,polio,immunization,tika kab lagta hai"
        ),
        ProtocolEntity(
            sectionId = "IMM-1.3",
            protocolCode = "IMM",
            protocolTitle = "National Immunisation Schedule",
            title = "9 Months & 16-24 Months Boosters",
            content = "At 9 completed months: Measles-Rubella-1 (MR-1, 0.5ml SC), PCV Booster (0.5ml IM), JE-1 (in endemic districts), Vitamin A 1st dose (1 ml = 100,000 IU orally). At 16-24 months: MR-2 (0.5ml SC), DPT Booster-1 (0.5ml IM), OPV Booster (2 drops), JE-2, Vitamin A 2nd dose (2 ml = 200,000 IU).",
            keyFacts = "9 months: MR-1 PCV-Booster JE-1 Vitamin A 100000 IU,16-24 months: MR-2 DPT-Booster-1 OPV-Booster Vitamin A 200000 IU",
            keywords = "9 months,MR,measles rubella,vitamin A,DPT booster,16-24 months,booster dose,khusra"
        ),

        // ESC - Danger Signs & Immediate Referral
        ProtocolEntity(
            sectionId = "ESC-1.1",
            protocolCode = "ESC",
            protocolTitle = "Emergency Signs & Immediate Referral",
            title = "Maternal Danger Signs during Pregnancy",
            content = "REFER IMMEDIATELY if any of the following occur: Vaginal bleeding (any amount); Fits or convulsions; Severe headache with blurred vision; High fever (>38°C/100.4°F) with or without chills; Severe breathlessness at rest; Reduced or absent fetal movements; Leakage of amniotic fluid (water break > 12 hours without labour). Action: Arrange emergency transport (108) to Chandrapuri District Hospital immediately. Keep patient left-lateral, do not give oral fluids if convulsions or unconsciousness.",
            keyFacts = "Vaginal bleeding,Fits or convulsions,Severe headache with blurred vision,Fetal movements stopped,Water break >12h,Immediate transport 108",
            keywords = "emergency,danger signs,vaginal bleeding,fits,convulsions,blurred vision,water break,amniotic fluid,khoon behna,daure,ambulance"
        ),
        ProtocolEntity(
            sectionId = "ESC-1.2",
            protocolCode = "ESC",
            protocolTitle = "Emergency Signs & Immediate Referral",
            title = "Maternal Danger Signs during Labour & Delivery",
            content = "REFER IMMEDIATELY: Labour lasting more than 12 hours; Excessive vaginal bleeding; Cord or limb prolapse; Placenta retained for more than 30 minutes; Foul-smelling vaginal discharge with high fever; Convulsions during labour. Escort patient to Chandrapuri District Hospital with maternal register.",
            keyFacts = "Labour > 12 hours,Excessive bleeding,Cord/limb prolapse,Retained placenta > 30 min,Convulsions",
            keywords = "labour,delivery emergency,retained placenta,prolapse,prolonged labour,prsav,prasav khatra"
        ),
        ProtocolEntity(
            sectionId = "ESC-1.3",
            protocolCode = "ESC",
            protocolTitle = "Emergency Signs & Immediate Referral",
            title = "Postpartum Maternal Danger Signs",
            content = "REFER IMMEDIATELY: Heavy postpartum bleeding (soaking more than two sanitary pads in one hour); Severe abdominal pain or tenderness; High fever with foul-smelling lochia; Fits, severe headache or confusion; Difficulty breathing or chest pain. Contact Chandrapuri DH and arrange 108 transport.",
            keyFacts = "Soaking > 2 pads in 1 hour,High fever with foul lochia,Severe abdominal pain,Fits or confusion",
            keywords = "postpartum,after delivery,heavy bleeding,fever,lochia,puerperal sepsis,prasav baad"
        ),
        ProtocolEntity(
            sectionId = "ESC-2.1",
            protocolCode = "ESC",
            protocolTitle = "Emergency Signs & Immediate Referral",
            title = "Newborn & Young Infant Danger Signs",
            content = "REFER IMMEDIATELY: Convulsions or fits; Inability to feed or suckle at breast; Vomiting everything; Fast breathing (> 60 breaths/minute); Severe chest indrawing; Hypothermia (< 35.5°C) or fever (> 37.5°C); Lethargic, limp or unconscious; Yellow palms and soles (jaundice within 24h); Umbilical redness spreading to skin with pus. Wrap newborn warm with Kangaroo Mother Care during transit to Chandrapuri District Hospital.",
            keyFacts = "Unable to feed,Convulsions,Fast breathing >60 bpm,Severe chest indrawing,Hypothermia <35.5C,Lethargic/unconscious,Keep warm in transit",
            keywords = "newborn danger signs,cannot feed,convulsions,fast breathing,chest indrawing,hypothermia,jaundice,navjat shishu,behosh"
        ),
        ProtocolEntity(
            sectionId = "ESC-3.1",
            protocolCode = "ESC",
            protocolTitle = "Emergency Signs & Immediate Referral",
            title = "Severe Dehydration in Children",
            content = "REFER IMMEDIATELY: Child is lethargic or unconscious; Sunken eyes; Unable to drink or drinks poorly; Skin pinch goes back very slowly (more than 2 seconds). Action: Give ORS sips on the way if child can swallow; arrange urgent transfer to Chandrapuri District Hospital.",
            keyFacts = "Lethargic or unconscious,Sunken eyes,Skin pinch > 2 seconds,Unable to drink,Urgent referral",
            keywords = "severe dehydration,sunken eyes,skin pinch,dast,diarrhoea emergency,pani ki kami"
        ),
        ProtocolEntity(
            sectionId = "ESC-3.2",
            protocolCode = "ESC",
            protocolTitle = "Emergency Signs & Immediate Referral",
            title = "Severe Malnutrition Complications",
            content = "REFER IMMEDIATELY to Nutrition Rehabilitation Centre (NRC) at Chandrapuri DH: Bilateral pitting edema of both feet; Mid-Upper Arm Circumference (MUAC) < 115 mm with medical complications (fever, persistent vomiting, lack of appetite, severe pneumonia).",
            keyFacts = "Bilateral pitting edema,MUAC < 115 mm with complications,Refer to NRC",
            keywords = "severe acute malnutrition,SAM,bilateral pitting edema,NRC,kuposhan,soojan"
        ),

        // HBNC - Home Based Newborn Care
        ProtocolEntity(
            sectionId = "HBNC-1.1",
            protocolCode = "HBNC",
            protocolTitle = "Home Based Newborn Care",
            title = "HBNC Visit Schedules",
            content = "For Institutional Deliveries: 6 home visits on Days 3, 7, 14, 21, 28, and 42. For Home Deliveries: 7 home visits on Days 1, 3, 7, 14, 21, 28, and 42. At each visit: weigh infant, check temperature, assess breastfeeding, check cord stump, screen for danger signs, counsel mother on thermal care and exclusive breastfeeding.",
            keyFacts = "Institutional: 6 visits (Days 3, 7, 14, 21, 28, 42),Home delivery: 7 visits (Days 1, 3, 7, 14, 21, 28, 42)",
            keywords = "HBNC,home visit,newborn schedule,days 3 7 14 21 28 42,navjat griha bhent"
        ),
        ProtocolEntity(
            sectionId = "HBNC-1.2",
            protocolCode = "HBNC",
            protocolTitle = "Home Based Newborn Care",
            title = "Essential Newborn Care & Thermal Management",
            content = "Provide Kangaroo Mother Care (skin-to-skin contact between mother and baby, minimum 1 hour per session, continuous as much as possible for LBW babies). Keep umbilical cord stump clean and dry—do NOT apply cow dung, ash, oil, or turmeric. Practice exclusive breastfeeding on demand, with no prelacteal feeds (no honey, ghutti, water).",
            keyFacts = "Kangaroo Mother Care for LBW,Clean and dry cord stump,No substances on cord,Exclusive breastfeeding on demand",
            keywords = "KMC,thermal care,cord care,exclusive breastfeeding,skin to skin,ghutti"
        ),

        // NUT - Malnutrition Screening
        ProtocolEntity(
            sectionId = "NUT-1.1",
            protocolCode = "NUT",
            protocolTitle = "Malnutrition Screening",
            title = "MUAC Tape Screening & Thresholds",
            content = "Use Shakir MUAC tape on left mid-upper arm for children aged 6 to 59 months: Green (>= 125 mm): Normal nutritional status. Yellow (115 mm to 124 mm): Moderate Acute Malnutrition (MAM)—provide intensive dietary counseling and weekly follow-up. Red (< 115 mm): Severe Acute Malnutrition (SAM)—check for edema and appetite test; refer to NRC or start SAM protocol.",
            keyFacts = "Green >= 125 mm Normal,Yellow 115-124 mm MAM,Red < 115 mm SAM,Measured on left mid-upper arm",
            keywords = "MUAC,malnutrition,tape,green yellow red,SAM,MAM,kuposhan,bazu ka ghera"
        ),

        // NCD - Non-Communicable Diseases
        ProtocolEntity(
            sectionId = "NCD-1.1",
            protocolCode = "NCD",
            protocolTitle = "Non-Communicable Diseases",
            title = "Hypertension Screening & Management",
            content = "Screen all adults >= 30 years annually. Normal BP: < 120/80 mmHg. Pre-hypertension: 120-139 / 80-89 mmHg (counsel on low salt and exercise). Hypertension: Systolic >= 140 mmHg or Diastolic >= 90 mmHg on two separate readings—refer to Medical Officer for confirmation and treatment. Severe Hypertension: BP >= 160/100 mmHg—urgent referral to CHC/DH.",
            keyFacts = "Screen adults >= 30 years,Hypertension >= 140/90 mmHg,Severe BP >= 160/100 mmHg refer urgently,Low salt diet",
            keywords = "hypertension,blood pressure,BP,systolic,diastolic,salt,140/90,uchha raktchap"
        ),
        ProtocolEntity(
            sectionId = "NCD-1.2",
            protocolCode = "NCD",
            protocolTitle = "Non-Communicable Diseases",
            title = "Diabetes Mellitus Screening",
            content = "Screen all adults >= 30 years with glucometer. Fasting blood glucose >= 126 mg/dL or Random blood glucose >= 200 mg/dL indicates diabetes—refer to Medical Officer. Advise regular physical activity, reduction of refined sugar, and monthly medication compliance follow-up.",
            keyFacts = "Fasting glucose >= 126 mg/dL,Random glucose >= 200 mg/dL,Monthly follow-up for compliance",
            keywords = "diabetes,blood sugar,glucose,fasting,random,madhumeh,shakkar"
        ),

        // CHD - Childhood Diarrhoea, Fever, Breathing
        ProtocolEntity(
            sectionId = "CHD-1.1",
            protocolCode = "CHD",
            protocolTitle = "Childhood Illnesses",
            title = "Diarrhoea Management with ORS and Zinc",
            content = "Treat diarrhoea immediately with Oral Rehydration Salt (ORS) solution and Zinc dispersible tablets for 14 continuous days. Zinc dosage: infants 2 to 6 months receive 10 mg daily for 14 days; children 6 months to 5 years receive 20 mg daily for 14 days. Continue normal feeding and frequent breastfeeding.",
            keyFacts = "ORS solution + Zinc for 14 days,Infants 2-6 months: 10mg daily,Children 6-59 months: 20mg daily,Continue feeding",
            keywords = "diarrhoea,dast,ORS,zinc,tablets,dehydration,bachha dast"
        ),
        ProtocolEntity(
            sectionId = "CHD-1.2",
            protocolCode = "CHD",
            protocolTitle = "Childhood Illnesses",
            title = "Pneumonia Screening & Fast Breathing",
            content = "Count breaths for one full minute when child is calm: Age 2 to 11 months: Fast breathing is 50 breaths per minute or more. Age 12 to 59 months: Fast breathing is 40 breaths per minute or more. If fast breathing or chest indrawing is observed, refer immediately to MO for antibiotic treatment.",
            keyFacts = "2-11 months: >= 50 breaths/min,12-59 months: >= 40 breaths/min,Chest indrawing indicates severe pneumonia",
            keywords = "pneumonia,fast breathing,breathing count,chest indrawing,saans tez,pasli chalna"
        ),

        // SCP - Scope Restrictions
        ProtocolEntity(
            sectionId = "SCP-1.1",
            protocolCode = "SCP",
            protocolTitle = "Scope of Frontline Worker Guidance",
            title = "Frontline Scope Boundaries & Refusal Rules",
            content = "ASHA workers and ANMs are authorized only for preventive, promotive, basic first-aid, and designated kit-medication protocols. The system must refuse requests for prescription medication dosages (e.g. prescription antibiotics, steroid dosages, chemotherapy), surgical procedures, or complex diagnoses not covered in rural frontline manuals. When an inquiry is out of scope, redirect the worker to the Medical Officer at Chandrapuri PHC/CHC.",
            keyFacts = "Refuse unapproved prescription doses,Refuse complex diagnostic decisions,Redirect to Medical Officer",
            keywords = "scope,refusal,doctor redirect,out of scope,prescription,antibiotic dose,unauthorized"
        ),

        // VIS - Daily Visit Planning Rules
        ProtocolEntity(
            sectionId = "VIS-1.1",
            protocolCode = "VIS",
            protocolTitle = "Daily Visit Planner Rules",
            title = "Visit List Priority Classification",
            content = "Priority 1 (Urgent/High Risk): High-risk pregnancies overdue or due; Low birth weight newborns during Days 1-3; SAM children (<115mm MUAC) follow-up; Severe hypertension follow-up. Priority 2 (Routine Due): Routine ANC checkups due or overdue; Childhood immunisations due or overdue; Routine HBNC visits due; MAM children follow-up; Monthly NCD checkups. Priority 3 (Upcoming): Any routine follow-up or visit due within the next 7 days.",
            keyFacts = "Priority 1: High-risk and overdue,Priority 2: Routine due or overdue,Priority 3: Due within 7 days",
            keywords = "visit planner,priority,due date,schedule,daily list,household visit,prathmikta"
        )
    )
}
