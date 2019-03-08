python sdp.py
ffplay -protocol_whitelist file,udp,rtp -probesize 32 -sync ext file.sdp
PAUSE