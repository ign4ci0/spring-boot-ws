As UGICOFR:
mkdir /apps/COFR/sidi-emulator
chown -R UGICOFR:COFR /apps/COFR/sidi-emulator/
chmod -R 774 /apps/COFR/sidi-emulator/

As root:
firewall-cmd --zone=public --add-port=9909/tcp --permanent
 firewall-cmd --reload

As USMCOFR:
mkdir /apps/COFR/logs/sidi-emulator

→ cat > sidi.properties 
server.port=9909
server.contextPath=/sidi-server/services/ConsultasWS
sidi.source=file
sidi.file=sididata.properties


→ cat > sididata.properties 
#Thu Nov 29 09:50:31 CET 2018
1.1.173.2=948948602,1111948948602,1111948948602
1.1.173.1=TEL_0000000,ADMIN_0000000,ICCR_0000000

java -jar sidi-emulator-2.0.0-SNAPSHOT.jar --spring.config.location=file:sidi.properties >> /apps/COFR/logs/sidi-emulator/cofre.log &