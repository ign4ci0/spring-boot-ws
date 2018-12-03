
→ cat > ws.properties 
server.port=9909
server.contextPath=/ws/services/ConsultasWS
ws.source=file
ws.file=wsdata.properties


→ cat > wsdata.properties 
#Thu Nov 29 09:50:31 CET 2018
1.1.173.2=948948602
1.1.173.1=948948601

java -jar spring-boot-ws-1.0.0-SNAPSHOT.jar --spring.config.location=file:ws.properties &