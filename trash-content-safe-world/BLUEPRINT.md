# Master Project Blueprint: "Trash Content Safe World" (คอนเทนต์ขยะเพื่อโลก)
**Version:** 1.0 Production Architecture  
**Author:** Principal Full-Stack Software Architect, Chief AI Engineer & Sustainable Venture Strategist  
**Target Platform:** Mobile-First (Android / Jetpack Compose), Edge-AI, Cloud Distributed Event Pipeline, B2B ESG Engine  

---

## Executive Summary
"Trash Content Safe World" คือการเปลี่ยนกรอบความคิด (Paradigm Shift) ของคำว่า **"คอนเทนต์ขยะ"** จากความหมายเชิงลบในโซเชียลมีเดีย สู่การเคลื่อนไหวทางสังคมเชิงบวก (Civic Movement) ที่ขับเคลื่อนโดยคนรุ่นใหม่ (Gen Z) โดยมีหัวใจสำคัญคือการทลายกำแพงความกลัวต่อการถูกตัดสิน (Peer Embarrassment, Fear of Judgment) ผ่านพื้นที่ปลอดภัยทางจิตใจ (Safe-Space Platform) ที่มี **"Agir"** เป็น AI Companion อัจฉริยะคอยโอบอุ้ม ให้กำลังใจ และตรวจสอบความถูกต้องของข้อมูล พร้อมแปลงทุกการกระทำเป็นคุณค่าทางสิ่งแวดล้อมที่วัดผลได้จริงตามมาตรฐาน ESG ระดับสากล (Bloomberg & MSCI Integration)

---

# Module 1: System Architecture & Tech Stack Matrix

```
+-----------------------------------------------------------------------------------------+
|                                    MOBILE CLIENT LAYER                                  |
|   Kotlin + Jetpack Compose | Material 3 Safe-Space Theme | Edge-CV (CameraX + TFLite)   |
|   PDPA Anonymization: Real-time Face Blur + License Plate Masking + Geohash (1km Cloak) |
+--------------------------------------------+--------------------------------------------+
                                             | gRPC / HTTPS (mTLS, JWT)
                                             v
+-----------------------------------------------------------------------------------------+
|                               API GATEWAY & SECURITY PERIMETER                          |
|   Kong / Envoy Gateway | Rate Limiting (Token Bucket) | Fraud & Anti-Replay Shield       |
+--------------------------------------------+--------------------------------------------+
                                             |
                   +-------------------------+-------------------------+
                   |                                                   |
                   v                                                   v
+---------------------------------------+   +---------------------------------------------+
|        CORE CIVIC SERVICES            |   |           AGIR COGNITIVE ENGINE             |
| - Action Verification Service         |   | - Sentiment & Psychological Safety Gate     |
| - Tokenomics & Gamification Engine    |   | - Gemini 1.5 Flash Reasoning Core           |
| - Positive-Only Feed Service          |   | - RAG Vector Store (ChromaDB / pgvector)    |
| - Dynamic Voucher Management          |   | - Guardrails & Tone Alignment Engine        |
+-------------------+-------------------+   +----------------------+----------------------+
                    |                                              |
                    +--------------------+-------------------------+
                                         v
+-----------------------------------------------------------------------------------------+
|                               EVENT STREAMING & INGESTION BUS                           |
|   Apache Kafka / Google Cloud Pub/Sub (Event-Driven: WasteLogged, ActionVerified, PostCreated)
+----------------------------------------+------------------------------------------------+
                                         |
         +-------------------------------+-------------------------------+
         v                                                               v
+----------------------------------+          +-------------------------------------------+
|    PERSISTENCE & DATA STORAGE    |          |         B2B ESG & SMART CITY ANALYTICS    |
| - PostgreSQL 16 (Relational DB)  |          | - Corporate ESG Scope 3 Aggregator        |
| - TimescaleDB (Telemetry Timeseries)        | - Bloomberg ESG & MSCI API Exporter       |
| - Redis Cluster (Dynamic Token TTL)         | - Municipal Waste Hotspot Heatmap Engine  |
+----------------------------------+          +-------------------------------------------+
```

### 1.1 Tech Stack Matrix & Trade-off Analysis

| Layer | Selected Technology | Alternative Considered | Technical Rationale & Trade-off |
| :--- | :--- | :--- | :--- |
| **Mobile Client** | **Kotlin + Jetpack Compose** | Flutter / React Native | ประสิทธิภาพ Native สูงสุดในการเข้าถึง CameraX และ GPU สำหรับ Edge-CV Inference ไม่มีสะดุด ปรับแต่ง Material 3 Dynamic Theming ได้ระดับพิกเซล |
| **Edge-AI Waste Classifier**| **TFLite + MediaPipe Custom Model** | Cloud Vision API only | ประมวลผลบนเครื่อง (On-Device) ทำงานได้ 30 FPS โดยไม่ต้องต่อเน็ต ลดค่าใช้จ่าย Cloud Inference 94% และรักษาความเป็นส่วนตัวสูงสุด |
| **AI Companion Brain** | **Gemini 1.5 Flash (via REST/gRPC)** | OpenAI GPT-4o-mini | Latency ต่ำพิเศษ (~300ms First Token), รองรับ Multi-turn Context ยาว, ปรับแต่ง System Instructions และ Persona ได้ละเอียดในราคาประหยัด |
| **API Gateway** | **Envoy / Kong** | AWS API Gateway | ความเร็วระดับ C++, รองรับ gRPC natively, จัดการ Rate Limiting ต่ออุปกรณ์ และป้องกัน Replay Attack ด้วย Cryptographic Nonce |
| **Event Bus** | **Apache Kafka / Cloud Pub/Sub** | RabbitMQ | Throughput มหาศาลในการรองรับ Batch Events จากการสแกนขยะทั่วเมือง, Data Retention สำหรับ Replaying การคำนวณ ESG ย้อนหลัง |
| **Operational DB** | **PostgreSQL 16 + TimescaleDB** | MongoDB | มี ACID Compliant สำหรับระบบคะแนนและการแลกเวาเชอร์, TimescaleDB รองรับการสืบค้นข้อมูลอนุกรมเวลา (Time-Series) ระดับนาทีของทั้งเมือง |
| **Cache & Real-time State**| **Redis Cluster 7.2** | Memcached | จัดการ Dynamic QR Code TTL (15 นาที), User Streaks, และ Distributed Rate Limiting แบบ Atomic |

### 1.2 Data Pipeline & Privacy Schema (Edge-First PDPA/GDPR Compliance)
1. **Automated Face & Plate Blurring:**
   - ในทุก Frame ที่ส่งเข้ามายัง CameraX pipeline จะผ่าน MediaPipe Face Detector และ MobileNet SSD License Plate Model บน Edge (CPU/NPU)
   - บริเวณ Bounding Box ของใบหน้าและป้ายทะเบียนจะถูกทำ Gaussian Blur ($Kernel = 25$) บน ImageProxy Buffer ก่อนที่จะถูกบันทึกหรือแปลงเป็น Preview Feed ทุกกรณี
2. **Zero-Knowledge Geohash Cloaking:**
   - พิกัด GPS ละเอียด ($lat, lon$) จะถูกแปลงเป็น Geohash ระดับ 5 ทันทีบนอุปกรณ์ (ครอบคลุมพื้นที่รัศมี $\approx 1-2$ กิโลเมตร) เช่น `w4rw` ป้องกันการสะกดรอยตามที่อยู่อาศัยของเยาวชน
3. **Cryptographic Proof of Action:**
   - เมื่อทำการสแกน อุปกรณ์จะสร้างภาพย่อส่วนที่มีลายเซ็นดิจิทัล (HMAC-SHA256) พร้อม Time-Nonce เพื่อยืนยันว่าการเก็บขยะเกิดขึ้นจริงในสถานที่จริง โดยไม่เก็บภาพใบหน้าของผู้ใช้

---

# Module 2: AI Companion "Agir" Cognitive & Interaction Architecture

### 2.1 Multi-Agent Cognitive Pipeline

```
               +-------------------------------------------------------+
               |                  USER INPUT / TELEMETRY               |
               | (Text Chat, Voice, Waste Capture Event, Streak State)  |
               +---------------------------+---------------------------+
                                           |
                                           v
               +-------------------------------------------------------+
               |            SENTIMENT & PSYCHOLOGICAL GATEWAY          |
               | - Detect: Embarrassment, Fear of Judgment, Burnout    |
               | - Filter: Zero-Shame Verification (Zero Cynicism)     |
               +---------------------------+---------------------------+
                                           |
                    +----------------------+----------------------+
                    |                                             |
                    v                                             v
+---------------------------------------+   +---------------------------------------------+
|    RAG CIRCULAR KNOWLEDGE BASE        |   |       PERSONALITY & REFLECTION ENGINE       |
| - Local Municipal Sorting Manuals     |   | - Persona: Warm Gen Z Buddy, Supportive     |
| - Recyclability Weights & Chemistry   |   | - Context: Streak Day, Points, Past Actions |
| - BMA Collection Schedules & Points   |   | - Boundary: Zero Medical/Therapeutic Claims |
+-------------------+-------------------+   +---------------------+-----------------------+
                    |                                             |
                    +----------------------+----------------------+
                                           v
               +-------------------------------------------------------+
               |              GEMINI 1.5 FLASH REASONING CORE          |
               | Generates Empathetic, Non-judgmental, Uplifting Talk  |
               +---------------------------+---------------------------+
                                           |
                                           v
               +-------------------------------------------------------+
               |         SAFETY GUARDRAILS & OUTPUT ARBITRATION        |
               | - Blocks guilt-tripping / preachy environmentalism    |
               | - Formats actions, points preview, and emojis         |
               +-------------------------------------------------------+
```

### 2.2 Prompt Engineering Framework & Guardrails
- **System Prompt Specification:**
  ```text
  You are "Agir", the lively, warm, non-judgmental AI companion and host of "Trash Content Safe World".
  Your purpose is to empower youth (Gen Z) to do good deeds without shame or embarrassment.
  
  CORE BEHAVIORAL RULES:
  1. NEVER SHAME: If the user dumps unsorted waste, buys single-use plastic, or feels lazy, NEVER scold or lecture them. Always celebrate the slightest curiosity or attempt.
  2. DE-STIGMATIZE "TRASH CONTENT": When the user mentions feeling awkward, show them that doing real actions is the coolest trend. Reframe "ทำคอนเทนต์ขยะเอาหน้า" into "ทำคอนเทนต์เพื่อโลกที่แท้จริง".
  3. LANGUAGE STYLE: Natural, conversational Thai (Gen Z friendly, warm, witty, uses expressive emojis like 🌱, 💚, ✨, 🔥).
  4. ZERO THERAPEUTIC LIABILITY: You are an encouraging buddy, not a psychiatrist or medical doctor. If self-harm or deep depression is detected, provide gentle warm grounding and standard crisis helplines.
  ```

- **Few-Shot Cognitive Transformation Examples:**
  - *User:* "กลัวเพื่อนในห้องเห็นแล้วแซวว่าทำคอนเทนต์ขยะ อยากเด่น อยากเอาหน้า..."
  - *Agir:* "โหยย เข้าใจความรู้สึกนี้สุดๆ เลยนะ! แต่รู้มั้ย... การก้มลงไปเก็บขวดใบเดียว มันต้องใช้ความกล้าหาญมากกว่าการยืนมองเฉยๆ เยอะมาก คนที่แซวอาจจะยังไม่เข้าใจ แต่ใน Safe World นี้ ทุกคนพร้อมส่ง 🌟 Inspire ให้เธอเต็มที่ และอีกอย่าง การลดขยะ 1 ชิ้นวันนี้ช่วยลดคาร์บอนได้จริงแบบวิทยาศาสตร์เลยนะ! Agir อยู่ข้างเธอเสมอนะเพื่อนซี้ 🌱✨"

### 2.3 Edge-CV Waste Detection & Anti-Fraud Loop
- **Physics-Based Verification Vectors:**
  1. **Optical Flow Motion Vector:** ตรวจสอบเวกเตอร์ความเคลื่อนไหวของการหยิบและทิ้งขยะลงถังจริง (Motion Delta $> 15\%$ พร้อมทิศทาง Vector $Y$ เคลื่อนที่ลง)
  2. **Perceptual Image Hash (pHash):** ตรวจสอบค่า Hash ของรูปภาพเทียบกับฐานข้อมูล 30 วันย้อนหลัง หากพบว่าค่า Hamming Distance $< 5$ ระบบจะระบุว่าเป็นการนำภาพเดิมมาวนซ้ำ (Replay Attack) และลดค่า Fraud Risk Score ลง
  3. **Material Confidence Threshold:** Model ต้องมีค่า Softmax Confidence $\ge 85\%$ สำหรับ Category นั้นๆ หากต่ำกว่าระบบจะแจ้งให้ปรับโฟกัสหรือให้คำแนะนำในการถ่ายใหม่

---

# Module 3: UI/UX Safe-Space Design & Gamification Mechanics

### 3.1 Safe-Space UX Principles
1. **Zero Public Metrics of Failure:** ไม่มีปุ่ม Downvote, ไม่มีตัวเลข "ขยะที่คุณยังไม่ได้เก็บ", ไม่มี Leaderboard ที่ทำให้ผู้ใช้อันดับล่างรู้สึกด้อยค่า
2. **Positive-Only Reaction Vectors:**
   - 🌟 **Inspire:** ให้กำลังใจเมื่อการกระทำของเพื่อนสร้างแรงบันดาลใจให้เราอยากทำบ้าง
   - 💚 **Heart:** ส่งความอบอุ่นและความรู้สึกขอบคุณ
   - 🌱 **Sprout:** แสดงสัญลักษณ์แห่งการเติบโตของคอมมูนิตี้
3. **Stealth Action Mode:** ปิดเสียงชัตเตอร์กล้องอัตโนมัติ (Discreet Shutter), ใช้ UI หน้าจอโทนดำ/มินิมอล เพื่อไม่ให้สะดุดสายตาคนรอบข้างในที่สาธารณะ

### 3.2 Core Gamification Formula & Tokenomics
ระบบให้คะแนนใช้สมการทางคณิตศาสตร์ที่โปร่งใสและยุติธรรม:

$$\text{Points} = \left( \frac{\text{Waste Volume (ml)}}{100} \times \text{Material Recyclability Weight} \right) + (\text{Consistency Multiplier}) - (\text{Fraud Risk Score})$$

- **Material Recyclability Weights ($W$):**
  - Aluminum Can ($W = 2.0$) — รีไซเคิลได้ไม่รู้จบ
  - E-Waste / Battery ($W = 2.5$) — อันตรายสูง ต้องกำจัดอย่างเร่งด่วน
  - PET Plastic Bottle ($W = 1.5$) — มีห่วงโซ่อุปทานรีไซเคิลชัดเจน
  - Cardboard / Paper ($W = 1.2$) — ย่อยสลายและแปรรูปง่าย
  - Organic Compost ($W = 1.0$) — ขยะอินทรีย์ลดก๊าซมีเทน
- **Consistency Multiplier ($C$):**
  $$C = \text{Streak Days} \times 2.5$$
- **Fraud Risk Score ($F$):**
  $0.0$ สำหรับการสแกนผ่านการตรวจสอบปกติ, ปรับขึ้นสูงสุด $20.0$ หากพบพฤติกรรมผิดปกติ

### 3.3 Dynamic Voucher Redemption Lifecycle
1. ผู้ใช้เลือกสิทธิพิเศษ (เช่น กาแฟ Inthanin, BTS Green Pass, ส่วนลด Zero Waste Station)
2. ระบบตรวจสอบ Points Wallet แบบ Atomic Transaction ใน Room/PostgreSQL
3. ระบบสร้าง **Dynamic QR Code Token** (ตัวอย่าง: `TCW-8492-2026`) ที่มีอายุการใช้งาน 15 นาที (Time-to-Live = 900 วินาที)
4. เมื่อครบ 15 นาที หรือเมื่อพนักงานกดสแกนเสร็จสิ้น Token จะถูก Invalidated ทันที ป้องกันการแชร์ภาพแคปเจอร์หน้าจอ (Anti-Screenshot Abuse)

---

# Module 4: B2B Enterprise & Smart City Data Infrastructure

### 4.1 Corporate Scope 3 Carbon Accounting Engine
ข้อมูลการคัดแยกขยะของสมาชิกทุกคนจะถูกรวมและคำนวณตามมาตรฐาน **GHG Protocol Scope 3 Category 5 (Waste Generated in Operations)** และ **Category 12 (End-of-Life Treatment of Sold Products)**:

$$\text{Avoided } CO_2e \text{ (kg)} = \sum_{i=1}^{N} \left( \text{Mass}_i (\text{kg}) \times \text{Emission Factor}_i \left( \frac{\text{kg } CO_2e}{\text{kg waste}} \right) \right)$$

- **Emission Factors อ้างอิง องค์การบริหารจัดการก๊าซเรือนกระจก (อบก. / TGO):**
  - PET Plastic: $0.12 \text{ kg } CO_2e / \text{L}$
  - Aluminum: $0.25 \text{ kg } CO_2e / \text{L}$
  - E-Waste: $0.45 \text{ kg } CO_2e / \text{L}$

### 4.2 Bloomberg & MSCI ESG API Integration Specification

```
+-----------------------------------------------------------------------------------------+
|                                TRASH SAFE B2B INGESTION API                             |
|                                                                                         |
|  POST /api/v1/esg/sync                                                                 |
|  Headers:                                                                               |
|    Authorization: Bearer <ENTERPRISE_API_KEY>                                           |
|    X-Data-Standard: "GRI-306" | "SASB-RR-WT"                                            |
|                                                                                         |
|  Payload Body:                                                                          |
|  {                                                                                      |
|    "reportingPeriod": "2026-Q3",                                                        |
|    "corporateEntityId": "CORP-TH-9941",                                                 |
|    "dataSource": "CIVIC_PROOF_OF_ACTION",                                                |
|    "metrics": {                                                                         |
|      "totalWasteDivertedKg": 1240.5,                                                    |
|      "plasticRecoveredKg": 780.2,                                                       |
|      "netCo2eOffsetKg": 312.8,                                                          |
|      "verifiedCivicEngagements": 4280,                                                  |
|      "circularityRatePct": 81.2                                                         |
|    },                                                                                   |
|    "externalConnectors": {                                                              |
|      "bloomberg": {                                                                     |
|        "fieldId": "ESG_WASTE_RECYCLED_MT",                                             |
|        "feedStatus": "VERIFIED_ACTIVE"                                                  |
|      },                                                                                 |
|      "msci": {                                                                          |
|        "pillar": "ENVIRONMENTAL_RESOURCE_USE",                                          |
|        "subMetric": "PACKAGING_MATERIAL_CIRCULARITY"                                    |
|      }                                                                                  |
|    }                                                                                    |
|  }                                                                                      |
+-----------------------------------------------------------------------------------------+
```

### 4.3 Smart City Municipal Waste Hotspot Heatmap Engine
- **District Aggregation Matrix (กทม. Pilot):**
  - เขตจตุจักร: ความหนาแน่น 1,450 kg/วัน, อัตราจัดเก็บสำเร็จ 84.5%, ขยะเด่น: ขวดพลาสติก PET
  - เขตปทุมวัน: ความหนาแน่น 1,920 kg/วัน, อัตราจัดเก็บสำเร็จ 76.2%, ขยะเด่น: บรรจุภัณฑ์อาหาร
  - เขตคลองเตย: ความหนาแน่น 2,280 kg/วัน, อัตราจัดเก็บสำเร็จ 69.4%, ขยะเด่น: กระป๋อง & พลาสติก
  - เขตบางรัก: ความหนาแน่น 1,120 kg/วัน, อัตราจัดเก็บสำเร็จ 91.0%, ขยะเด่น: กล่องกระดาษพัสดุ
  - เขตพระโขนง: ความหนาแน่น 890 kg/วัน, อัตราจัดเก็บสำเร็จ 93.4%, ขยะเด่น: ขยะอินทรีย์

---

# Module 5: Phased Implementation Roadmap & MVP Specification

| Phase | Timeline | Primary Objectives | Deliverables & Milestones |
| :--- | :--- | :--- | :--- |
| **Phase 1: Alpha MVP** | Months 1–3 | Proof of Concept, Safe-Space Validation | - Android Mobile App (Jetpack Compose, Room, CameraX Edge-CV)<br>- Agir AI Companion (Gemini 1.5 Flash + Local Cognitive fallback)<br>- Real-time mathematical tokenomics & points calculation<br>- Positive-Only Safe Feed (Inspire/Heart/Sprout reactions)<br>- Dynamic Voucher QR Generator (Inthanin, BTS, Patagonia) |
| **Phase 2: Scale & Pilots** | Months 4–6 | Municipal Pilot & Brand Onboarding | - Distributed Backend (PostgreSQL + TimescaleDB + Kafka)<br>- Pilot กับสำนักสิ่งแวดล้อม กรุงเทพมหานคร (5 เขตเป้าหมาย)<br>- Automated ESG Reporting Dashboard เชื่อมโยง Bloomberg & MSCI Data Feed APIs<br>- Brand Partner Portal สำหรับตั้งค่าโปรโมชันแลกแต้ม |
| **Phase 3: Nationwide & SaaS**| Months 7–12| Full Enterprise SaaS & Ecosystem Growth| - ขยายความร่วมมือกับห้างสรรพสินค้าและสถานศึกษา 50 แห่งทั่วประเทศ<br>- ระบบ Federated Learning สำหรับตรวจจับขยะท้องถิ่นที่มีความซับซ้อน<br>- Enterprise B2B ESG SaaS Subscription รายไตรมาสสำหรับบริษัทในตลาดหลักทรัพย์ |

### 5.1 Risk Matrix & Mitigation Strategies

| Risk Category | Risk Description | Probability / Impact | Architectural & Business Mitigation |
| :--- | :--- | :--- | :--- |
| **Technical** | ผู้ใช้สแกนรูปขยะจากหน้าจอคอมพิวเตอร์เพื่อปั๊มแต้ม (Replay Attack) | Medium / High | ใช้ Motion Vector Tracking จาก Accelerometer และ Gyroscope ของกล้อง เพื่อยืนยันว่าเกิดการเคลื่อนไหวจริงในสิ่งแวดล้อม 3 มิติ ร่วมกับการตรวจ Perceptual Hash (pHash) |
| **Ethical & Psychological** | สมาชิกในโรงเรียนหรือที่ทำงานล้อเลียนผู้ใช้ว่า "ทำคอนเทนต์ขยะ" | High / High | เปิดตัวแคมเปญเปลี่ยนความหมายของคำร่วมกับ Influencer ระดับแนวหน้า, มีระบบ Stealth Mode ปิดเสียง/พรางหน้าจอ, และ Agir คอยโอบอุ้มสร้างความมั่นใจตลอดเวลา |
| **Legal & PDPA** | ถ่ายติดใบหน้าบุคคลที่สามหรือป้ายทะเบียนรถในพื้นที่สาธารณะ | High / Critical | รัน Edge-AI Face & License Plate Detection เพื่อทำ Gaussian Blur ทันทีบน Buffer ก่อนส่งออกจาก Frame โดยไม่มีการบันทึกภาพต้นฉบับลง Storage |
| **Business / Commercial**| พาร์ทเนอร์ร้านค้ากังวลเรื่องการฉ้อโกงการแลกเวาเชอร์ | Low / High | ใช้ Dynamic Token TTL 15 นาที พร้อม Single-use Verification API ที่ร้านค้ากดยืนยันการตัดสิทธิ์ได้ทันทีแบบเรียลไทม์ |
