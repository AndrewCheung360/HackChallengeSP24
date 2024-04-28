import requests
import json


def grab_json(url):
    return json.loads(requests.get(url).text)


root = "https://classes.cornell.edu/api/2.0"

rosterURL = root+"/config/rosters.json"
rosters = grab_json(rosterURL)['data']['rosters']  # gets roster data
roster = rosters[-1]['slug']  # gets current roster semester SP24

subjectsURL = root+"/config/subjects.json?roster="+roster
# get all subjects for SP24
subjects = grab_json(subjectsURL)['data']['subjects']

# Get json for all classes
classesJSON = []
for s in subjects:
    print(s)
    search = "/search/classes.json?roster="+roster+"&subject="+s['value']
    classURL = root+search
    classesJSON.extend(grab_json(classURL)['data']['classes'])

# Gets list of classes
classes = []
for c in classesJSON:
    item = {
        "code": c['subject'] + " " + c["catalogNbr"],
        "title": c['titleLong'],
        "name": c['subject'] + " " + c["catalogNbr"] + ": " + c["titleLong"],
        "desc": c["description"]
    }
    print(item)
    classes.append(item)
