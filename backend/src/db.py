from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

#TODO: Association tables
user_course_association_table = db.Table("user_course_assoc", db.Model.metadata,
    db.Column("user_id", db.Integer, db.ForeignKey("user.id")),
    db.Column("course_id", db.Integer, db.ForeignKey("course.id"))
)

#TODO: Models

"""
User class
"""
class User(db.Model):
  __tablename__ = "user"
  id = db.Column(db.Integer, primary_key = True)
  name = db.Column(db.String, nullable = False)
  profile_image = db.Column(db.String, nullable = False) #URL to profile image
  posted_notes = db.relationship("Note", cascade = "delete")
  courses = db.reltationship("Course", secondary = user_course_association_table, back_populates = "students")

  #TODO: Add serialization methods for user

"""
Course class
"""
class Course(db.Model):
  __tablename__ = "course"
  id = db.Column(db.Integer, primary_key = True)
  code = db.Column(db.String, nullable = False)
  name = db.Column(db.String, nullable = False)
  notes = db.relationship("Note", cascade = "delete")
  students = db.relationship("User", secondary = user_course_association_table, back_populates = "courses")

  #TODO: Add serialization methods for course

"""
Notes class
"""
class Note(db.Model):
  __tablename__ = "note"
  id = db.Column(db.Integer, primary_key = True)
  title = db.Column(db.String, nullable = False)
  course_id = db.Column(db.Integer, db.ForeignKey("course.id"), nullable = False)
  poster_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable = False)
  pdf_link = db.Column(db.String, nullable = False)

  #TODO: Add serialization methods for note