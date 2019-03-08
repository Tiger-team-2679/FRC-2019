import socket
address = socket.gethostbyname('tegra-ubuntu.local')

file = open("file.sdp","w") 
 
file.write("v=0\n")
file.write("t=0 0\n")
file.write("c=IN IP4 ")
file.write(address)
file.write("\n")
file.write("m=video 1234 RTP/AVP 96\n")
file.write("a=rtpmap:96 H264/1000000")
 
file.close() 