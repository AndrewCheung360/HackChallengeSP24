from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

# TODO: Association tables
user_course_association_table = db.Table("user_course_assoc", db.Model.metadata,
                                         db.Column("user_id", db.Integer,
                                                   db.ForeignKey("user.id")),
                                         db.Column(
                                             "course_id", db.Integer, db.ForeignKey("course.id"))
                                         )

# TODO: Models


class User(db.Model):
    """
    User class
    """
    __tablename__ = "user"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String, nullable=False)
    profile_image = db.Column(db.String, nullable=True)  # URL to profile image
    supabase_id = db.Column(db.String, nullable=False)
    email = db.Column(db.String, nullable=True)
    posted_notes = db.relationship("Note", cascade="delete")
    courses = db.relationship(
        "Course", secondary=user_course_association_table, back_populates="students")

    def __init__(self, **kwargs):
        """
        Initialize user object
        """
        self.name = kwargs.get("name")
        self.profile_image = kwargs.get("profile_image")
        self.supabase_id = kwargs.get("supabase_id")
        self.email = kwargs.get("email")

    def serialize(self):
        """
        Serializes user object
        """
        return {
            "id": self.id,
            "name": self.name,
            "avatar": self.profile_image,
            "supabaseId": self.supabase_id,
            "email": self.email,
            # "postedNotes": [n.serialize_non_recursive() for n in self.posted_notes],
            # "courses": [c.serialize_non_recursive() for c in self.courses]
            "notes": [n.id for n in self.posted_notes],
            "courses": [c.id for c in self.courses]
        }

    def serialize_non_recursive(self):
        """
        Serializes the user object such that it doesn't have the posted notes or courses
        """
        return {
            "id": self.id,
            "name": self.name,
            "avatar": self.profile_image,
            "supabaseId": self.supabase_id,
            "email": self.email,
        }

    # TODO make simple serialize to avoid infinite loops


class Course(db.Model):
    """
    Course class
    """
    __tablename__ = "course"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    code = db.Column(db.String, nullable=False)
    name = db.Column(db.String, nullable=False)
    description = db.Column(db.String, nullable=False)
    notes = db.relationship("Note", cascade="delete")
    students = db.relationship(
        "User", secondary=user_course_association_table, back_populates="courses")

    def __init__(self, **kwargs):
        """
        Initialize course object
        """
        self.code = kwargs.get("code")
        self.name = kwargs.get("name")
        self.description = kwargs.get("description")

    def serialize(self):
        """
        Serialize course object
        """
        return {
            "id": self.id,
            "code": self.code,
            "name": self.name,
            "description": self.description,
            # "notes": [n.serialize_non_recursive() for n in self.notes],
            # "students": [s.serialize_non_recursive() for s in self.students]
            "notes": [n.id for n in self.notes],
            "students": [s.id for s in self.students]
        }

    def serialize_non_recursive(self):
        """
        Serialize the course object such that it doesn't contain the notes or students
        """
        return {
            "id": self.id,
            "code": self.code,
            "name": self.name,
            "description": self.description
        }

    # TODO make simple serialize to avoid infinite loops


class Note(db.Model):
    """
    Notes class
    """
    __tablename__ = "note"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    title = db.Column(db.String, nullable=False)
    course_id = db.Column(db.Integer, db.ForeignKey(
        "course.id"), nullable=False)
    poster_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)

    def __init__(self, **kwargs):
        """
        Initialize notes object
        """
        self.title = kwargs.get("title")
        self.course_id = kwargs.get("course_id")
        self.poster_id = kwargs.get("poster_id")

    def serialize(self):
        """
        Serialize notes object
        """
        return {
            "id": self.id,
            "title": self.title,
            "course": Course.query.filter_by(id=self.course_id).first().serialize_non_recursive(),
            "poster": User.query.filter_by(id=self.poster_id).first().serialize_non_recursive(),
        }

    def serialize_non_recursive(self):
        """
        Serializes the notes object such thtat it contains course_id and poster_id
        rather than the course and poster
        """
        return {
            "id": self.id,
            "title": self.title,
            "courseId": self.course_id,
            "posterId": self.poster_id
        }

    # TODO make simple serialize to avoid infinite loops
