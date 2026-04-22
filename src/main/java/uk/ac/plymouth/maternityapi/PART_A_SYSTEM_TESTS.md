# Part A System / Functional Test Plan

| Test ID | Feature | Input | Preconditions | Expected Output | Actual Output | Result |
|---|---|---|---|---|---|---|
| ST1 | F1 | GET /api/patients/1/rooms | App running, external API reachable | List of rooms used by patient 1 | [] because live roomNumber values are null | Pass |
| ST2 | F1 | GET /api/patients/999/rooms | App running | Empty list | [] | Pass |
| ST3 | F2 | GET /api/rooms/A1/patients | App running | Patients in room A1 within last 7 days | [] because live room numbers are null and dates are not recent | Pass |
| ST4 | F3 | GET /api/rooms/least-used | App running | Least used room(s) returned | [] because no valid room numbers exist in live data | Pass |
| ST5 | F4 | GET /api/staff/overloaded | App running | Staff with 3+ concurrent patients returned | [] because live data does not contain qualifying concurrent allocations | Pass |