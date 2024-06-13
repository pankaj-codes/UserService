# Install Redis
Once you're running Ubuntu on Windows, you can follow the steps detailed at Install on Ubuntu/Debian to install recent stable versions of Redis from the official packages.redis.io APT repository. Add the repository to the apt index, update it, and then install:
``` bash
curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg
```
``` bash
echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list
```
``` bash
sudo apt-get update
sudo apt-get install redis
```
#Lastly, start the Redis server like so:

## Start Redis server :
``` bash
sudo service redis-server start
```
```
redis-cli ==> ping
```
Host - 127.0.0.1:6379
