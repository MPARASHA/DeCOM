import pymongo

client = pymongo.MongoClient("mongodb+srv://user1:user1@cluster0-ks5iw.mongodb.net/test?retryWrites=true&w=majority")
db = client.get_database('TEST')
records = db.TEST1

file = open("test1.txt","r")

f=file.readline().strip()

amount=[]
for x in records.find({'Item':f}):
    amount.append(float(x[f]))
if(len(amount)):
    index=amount.index(min(amount))
    print(records.find({'Item':f})[index])
else:
    print("No record of this item")
    