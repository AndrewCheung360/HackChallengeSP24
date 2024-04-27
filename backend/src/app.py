from db import db
from flask import Flask, request
import json

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
@app.route("/users/")
def get_all_users():
    """
    Endpoint for getting all users
    """

    users = [user.serialize() for user in User.query.all()]
    return success_response({"users":users})


@app.route("/users/", methods=["POST"])
def create_a_user():
    """
    Endpoint for creating a new user
    """
    body = json.loads(request.data)
    new_user = User(name = body.get("name"), profile_image = body.get("profile_image"))
    db.session.add(new_user)
    db.session.commit()
    return success_response(new_user.serialize(), 201)


@app.route("/user/<int:user_id>/")
def get_specific_user(user_id):
    """
    Endpoint for getting a task by id
    """
    user = User.query.filter_by(id=user_id).first()
    if user is None:
        return failure_response("User not found!")
    return success_response(user.serialize())


@app.route("/users/<int:user_id>/", methods=["POST"])
def update_task(user_id):
    """
    Endpoint for updating a user by id
    """
    body = json.loads(request.data)
    user = User.query.filter_by(id=user_id).first()

    if user is None:
        return failure_response("User not found!")
    user.name = body.get("name", task.name)
    user.profile_image = body.get("profile_image", task.profile_image)
    db.session.commit()
    return success_response(user.serialize())


@app.route("/users/<int:user_id>/", methods=["DELETE"])
def delete_user(user_id):
    """
    Endpoint for deleting a user by id
    """
    user = User.query.filter_by(id=user_id).first()
    if user is None:
        return failure_response("User not found!")

    db.session.delete(user)
    db.session.commit()
    return success_response(user.serialize())


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000, debug=True)
