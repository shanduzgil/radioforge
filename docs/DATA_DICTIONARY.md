# Data dictionary

| Field | Meaning | Availability |
|---|---|---|
| operator | Serving network operator name | Device-dependent |
| sim_operator | SIM operator identity | Permission/device dependent |
| network_type | Current data RAT | Usually available |
| cells[].ci/nci | LTE Cell Identity / 5G NR NCI | CellInfo permission/device dependent |
| cells[].pci | Physical Cell Identity | CellInfo permission/device dependent |
| cells[].tac | Tracking Area Code | CellInfo permission/device dependent |
| cells[].earfcn | LTE channel number | CellInfo permission/device dependent |
| cells[].nrarfcn | NR channel number | CellInfo permission/device dependent |
| cells[].rsrp | Reference signal received power | CellInfo permission/device dependent |
| cells[].rsrq | Reference signal received quality | CellInfo permission/device dependent |
| cells[].rssi | Received signal strength | CellInfo permission/device dependent |
| cells[].rssnr/ss_sinr | LTE RSSNR / NR SS-SINR | CellInfo permission/device dependent |
| transport | Active network transport | Usually available |
| local_ip | First non-IPv6 local address | Device-dependent |
| public_ip | Optional public address if network request succeeds | Network-dependent |
| ping_ms | ICMP latency to 1.1.1.1 | Network-dependent |
