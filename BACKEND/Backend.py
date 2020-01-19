import pymongo

client = pymongo.MongoClient("mongodb+srv://user1:user1@cluster0-ks5iw.mongodb.net/test?retryWrites=true&w=majority")
db = client.get_database('TEST')
records = db.TEST1


items=[]

file = open("test.txt","r")
f=1

while(f):
    f = file.readline()
    items.append(f.strip().split())
items = list(filter(None, items))
print(items)

for i in range(1, len(items)-1):
    new_records = {}
    x = records.find_one({'Item':items[i][0]})
    new_records['Store Address'] = " ".join(items[0])
    new_records["Item"]=items[i][0]
    new_records[items[i][0]]=items[i][1]
    records.insert_one(new_records)
