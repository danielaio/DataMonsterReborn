from pymongo import Connection

connection = Connection()
db = connection.mydb
collection = db.youngstersTweets

for tweet in collection.find():
	print(tweet['text'])