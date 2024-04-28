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
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String, nullable=False)
    profile_image = db.Column(
        db.String, nullable=False)  # URL to profile image
    posted_notes = db.relationship("Note", cascade="delete")
    courses = db.relationship(
        "Course", secondary=user_course_association_table, back_populates="students")

    def __init__(self, **kwargs):
        """
        Initialize user object
        """
        self.name = kwargs.get("name")
        self.profile_image = kwargs.get("profile_image")

    def serialize(self):
        """
        Serializes user object
        """
        return {
            "id": self.id,
            "name": self.name,
            "profile_image": self.profile_image,
            "posted_notes": [n.serialize() for n in self.posted_notes],
            "courses": [c.serialize() for c in self.courses]
        }

    # TODO make simple serialize to avoid infinite loops


class Course(db.Model):
    """
    Course class
    """
    __tablename__ = "course"
    id = db.Column(db.Integer, primary_key=True)
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
            "notes": [n.serialize() for n in self.notes],
            "students": [s.serialize() for s in self.students]
        }

    # TODO make simple serialize to avoid infinite loops


class Note(db.Model):
    """
    Notes class
    """
    __tablename__ = "note"
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String, nullable=False)
    course_id = db.Column(db.Integer, db.ForeignKey(
        "course.id"), nullable=False)
    poster_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    pdf_link = db.Column(db.String, nullable=False)

    def __init__(self, **kwargs):
        """
        Initialize notes object
        """
        self.title = kwargs.get("title")
        self.course_id = kwargs.get("course_id")
        self.poster_id = kwargs.get("poster_id")
        self.pdf_link = kwargs.get("pdf_link")

    def serialize(self):
        """
        Serialize notes object
        """
        return {
            "id": self.id,
            "title": self.title,
            "course": Course.query.filter_by(id=self.course_id).first().serialize(),
            "poster": User.query.filter_by(id=self.poster_id).first().serialize(),
            "pdf_link": self.pdf_link
        }

    # TODO make simple serialize to avoid infinite loops
