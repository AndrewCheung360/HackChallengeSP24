import requests
import json
import os


def grab_json(url):
    return json.loads(requests.get(url).text)


def retrieve_courses():
    """
    Uses cornell course api to retrieve a list of courses
    """
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
    return classesJSON


def store_courses(course_list):
    """
    Stores course_list into 'courses.json'
    """
    with open('courses.json', 'w') as f:
        json.dump(course_list, f)


def load_courses_from_file():
    """
    Loads the course list in 'courses.json'
    """
    try:
        # Load the list of courses from the JSON file
        with open('courses.json', 'r') as f:
            courses = json.load(f)
        return courses
    except FileNotFoundError:
        print("No courses file found.")
        return []


def serialize_courses(course_list):
    """
    Serializes each course in 'course_list'
    """
    classes = []
    for c in course_list:
        item = {
            "code": c['subject'] + " " + c["catalogNbr"],
            "title": c['titleLong'],
            "name": c['subject'] + " " + c["catalogNbr"] + ": " + c["titleLong"],
            "desc": c["description"]
        }
        print(item)
        classes.append(item)
    return classes


def add_courses_to_db(course_list):
    """
    Add courses to in course_list to the Courses Table. 
    Requires courses to be in serialized using serialize_courses.
    ONLY RUN ONCE TO PREVENT COURSES TABLE FROM HAVING DUPLICATE COURSES
    """
    local_host = "http://127.0.0.1:8000"
    deployed_route = "http://34.48.67.43"
    for c in course_list:
        r = requests.post(local_host+"/courses/",
                          data=json.dumps({
                              "code": c["code"],
                              "name": c["name"],
                              "description": c["desc"]
                          }))
        print("response is ", r)


# store courses
# course_list = retrieve_courses()
# store_courses(course_list)

# load courses
courses = serialize_courses(load_courses_from_file())
add_courses_to_db(courses)
# print(courses)
