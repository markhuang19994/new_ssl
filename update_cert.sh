#!/bin/bash
command_exists () {
    type "$1" &> /dev/null ;
}

if ! command_exists certbot-auto; then
  wget https://dl.eff.org/certbot-auto
  sudo mv certbot-auto /usr/local/bin/certbot-auto
  sudo chown root /usr/local/bin/certbot-auto
  sudo chmod 0755 /usr/local/bin/certbot-auto
fi

sudo /usr/local/bin/certbot-auto certonly --renew-by-default   --standalone  -q -d dummy.api.markhuang01.com

mkdir -p /tmp/mycert
cd /tmp/mycert || exit
sudo cp -rL /etc/letsencrypt/live/dummy.api.markhuang01.com .
cd dummy.api.markhuang01.com || exit
sudo chown "$USER":"$USER" -R .
sudo openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out aws.p12  -CAfile chain.pem  -passout pass:p@ssw0rd
sudo chown "$USER":"$USER" aws.p12

git clone --progress --verbose "https://markhuang19994:${GP}@github.com/markhuang19994/CITI_DUMMY_API.git"
sudo cp ./aws.p12 CITI_DUMMY_API/volume/data/store/key/aws.p12
cd CITI_DUMMY_API || exit
sudo git add volume/data/store/key/aws.p12
git commit -m 'update server certificate'
git push origin master

rm -rf /tmp/mycert
