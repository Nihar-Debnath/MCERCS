### MAKAUT Campus Emergency Response Coordination System (MCERCS)

## ✅ What This App Is For:

**Title:** MCERCS App (MAKAUT Campus Emergency Response Coordination System)

**Purpose:**  
This Java GUI-based system allows **students, faculty, responders, and admins** to:

1. **Report emergency incidents** (like medical, fire, natural disasters, etc.)
2. **Automatically assign a response team** if available.
3. **Mark incidents as resolved** and collect **feedback**.
4. **Track resolved incidents via logs**.
5. **Simulate communication between reporter and responder.**

---

## 🧠 Key Functionalities & Problem Solving:

### 1. 📝 **Emergency Reporting Interface**
- Users can report incidents by entering their name, selecting their role, emergency type, and location.
- Useful in **multi-campus institutions**, where emergency services must be triggered quickly and reliably.

### 2. 🧑‍🚒 **Dynamic Response Team Assignment**
- A team with a responder is **automatically assigned** to an incident.
- Sets the team as unavailable (`assign()`), and releases it after the resolution (`release()`).
- This models **real-life resource management** where emergency teams need to be dispatched and later freed.

### 3. 📬 **Communication System**
- Messages are simulated between the person reporting and the responder.
- Uses `Map<String, List<String>>` to simulate a messaging channel.
- **Improves transparency** in communication during emergencies.

### 4. ✅ **Incident Resolution + Feedback Collection**
- After the incident is resolved, feedback is collected from the user via a `JOptionPane`.
- The resolution is logged, and the team becomes available again.
- Helps maintain a **history of resolved cases** for administrative use.

### 5. 🗂️ **Incident Logs for Record Keeping**
- Logs are only shown after resolution.
- This mimics how **incident reports are reviewed by admins or responders** only after closure.

---

## 🛠️ Problems It Solves in Real Life:

| Problem | How It's Solved |
|--------|------------------|
| **Delayed emergency response** | Instantly assigns a team based on emergency type. |
| **Lack of communication** | Simulates messaging between user and responder. |
| **No centralized reporting system** | Unified GUI for all roles to report, resolve, and review. |
| **No incident history** | Maintains logs of all resolved cases. |
| **Resource mismanagement** | Team availability is tracked using `assign()` and `release()` methods. |

---

## 📦 Bonus Suggestions that i will implement in Future:

I will:

- Add **multiple teams**, and choose the nearest available one.
- Save logs/messages to files or a database.
- Add **user login and authentication** per role.
- Visualize logs using **tables or graphs**.
- Allow **admins to see unresolved incidents** too.
