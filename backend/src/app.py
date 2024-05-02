from db import db, User, Course, Note
from flask import Flask, request, url_for, redirect, session
import json
from authlib.integrations.flask_client import OAuth
import pyrebase

app = Flask(__name__)

db_filename = "notes.db"

app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///%s" % db_filename
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.config["SQLALCHEMY_ECHO"] = True

db.init_app(app)
with app.app_context():
    db.create_all()

# generalized responses


def success_response(body, code=200):
    return json.dumps(body), code


def failure_response(message, code=404):
    return json.dumps({'error': message}), code

# TODO: Routes


@app.route("/")
def hello_world():
    email = dict(session).get('email', None)
    return f'Hello {email}!'


@app.route("/users/")
def get_all_users():
    """
    Endpoint for getting all users
    """
    users = [user.serialize() for user in User.query.all()]
    return success_response({"users": users})


@app.route("/users/", methods=["POST"])
def create_a_user():
    """
    Endpoint for creating a new user
    Returns 400 error response if:
        - "name" or "profile_image" fields are missing
        - "name" or "profile_image" values not strings
    """
    body = json.loads(request.data)
    name, profile_image = body.get("name"), body.get("profile_image")
    # Data validation
    if name is None or profile_image is None:
        return failure_response("request body missing 'name' or 'profile_image' fields", 400)
    if not isinstance(name, str) or not isinstance(profile_image, str):
        return failure_response("'name' or 'profile_image' values not strings", 400)
    new_user = User(name=name, profile_image=profile_image)
    db.session.add(new_user)
    db.session.commit()
    return success_response(new_user.serialize(), 201)


@app.route("/user/<int:user_id>/")
def get_specific_user(user_id):
    """
    Endpoint for getting a task by id
    Returns 404 error response if user with user_id not found
    """
    user = User.query.filter_by(id=user_id).first()
    if user is None:
        return failure_response("User not found!")
    return success_response(user.serialize())


@app.route("/users/<int:user_id>/", methods=["POST"])
def update_task(user_id):
    """
    Endpoint for updating a user by id
    Returns 404 error response if user with user_id not found
    """
    body = json.loads(request.data)
    user = User.query.filter_by(id=user_id).first()
    # Data validation
    if user is None:
        return failure_response("User not found!")
    user.name = body.get("name", user.name)
    user.profile_image = body.get("profile_image", user.profile_image)
    db.session.commit()
    return success_response(user.serialize())


@app.route("/users/<int:user_id>/", methods=["DELETE"])
def delete_user(user_id):
    """
    Endpoint for deleting a user by id
    Returns 404 error response if user with user_id not found
    """
    user = User.query.filter_by(id=user_id).first()
    if user is None:
        return failure_response("User not found!")

    db.session.delete(user)
    db.session.commit()
    return success_response(user.serialize())


@app.route("/courses/")
def get_all_courses():
    """
    Endpoint for getting all courses
    """
    return success_response({"courses": [c.serialize() for c in Course.query.all()]})


@app.route("/courses/", methods=["POST"])
def create_course():
    """
    Endpoint for creating a course
    Returns 400 error response if:
        - "code", "name", or "description" fields are missing
        - "code", "name" or "description" values are not strings
    """
    body = json.loads(request.data)
    code, name, description = body.get("code"), body.get(
        "name"), body.get("description")
    # Data validation
    if code is None or name is None or description is None:
        return failure_response("request body missing 'code', 'name', or 'description' fields", 400)
    if not isinstance(code, str) or not isinstance(name, str) or not isinstance(description, str):
        return failure_response("'code', 'name', or 'description' value not of type string", 400)
    new_course = Course(code=code, name=name, description=description)
    db.session.add(new_course)
    db.session.commit()
    return success_response(new_course.serialize(), 201)


@app.route("/course/<int:course_id>/")
def get_course_by_id(course_id):
    """
    Endpoint for getting the course with 'course_id'
    Returns 404 error response if course with 'course_id' not found
    """
    course = Course.query.filter_by(id=course_id).first()
    if course is None:
        return failure_response("Course with 'course_id' not found")
    return success_response(course.serialize())

# OAuth Login and Authorize Routes -------------------------------------------


@app.route('/login/', methods=["POST"])
def login():
    body = json.loads(request.data)
    email = body.get("email")
    password = body.get("password")
    try:
        user = auth.sign_in_with_email_and_password(email, password)
        session["user"] = email
    except:
        return failure_response("failed to login", 200)

    return success_response({"email": email, "password": password})


@app.route('/logout/')
def logout():
    session.pop("user")
    return redirect('/')


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
