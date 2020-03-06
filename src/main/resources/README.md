###1.pkcs12私鑰產生
keytool -genkey -alias mark -dname "CN=llsl@mark.com,OU=llsl,O=llsl,L=XinYe,ST=Taipei,C=TW" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore markKeyStore.p12 -validity 365

####注意：llsl@mark.com，訪問時就必須用https://llsl@mark.com:443...

###2.憑證產生
keytool -export -alias mark -file markTrust.cer -keystore markKeyStore.p12 -storepass p@ssw0rd

###3.憑證匯入
keytool -import -v -trustcacerts -alias mark -keypass p@ssw0rd -file markTrust.cer
