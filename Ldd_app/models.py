from django.db import models


# Create your models here.
class login(models.Model):
    username = models.CharField(max_length=200)
    password = models.CharField(max_length=200)
    usertype = models.CharField(max_length=200)


class user(models.Model):
    name = models.CharField(max_length=200)
    age = models.CharField(max_length=200)
    email = models.CharField(max_length=200)
    phone = models.CharField(max_length=200)
    image = models.CharField(max_length=200)
    LOGIN = models.ForeignKey(login,default=1,on_delete=models.CASCADE)

class objectss(models.Model):
    name = models.CharField(max_length=200)
    image = models.CharField(max_length=200)


class equation(models.Model):
    equation = models.CharField(max_length=200)
    option_A = models.CharField(max_length=200, default="")
    option_B = models.CharField(max_length=200, default="")
    option_C = models.CharField(max_length=200, default="")
    option_D = models.CharField(max_length=200, default="")
    correct_answer = models.CharField(max_length=200, default="")


class feedback(models.Model):
    date = models.CharField(max_length=200)
    feedback = models.CharField(max_length=200)
    USER = models.ForeignKey(user, default=1, on_delete=models.CASCADE)


class exam(models.Model):
    date = models.CharField(max_length=200)
    results = models.CharField(max_length=200)
    category = models.CharField(max_length=200,default=1)
    USER = models.ForeignKey(user, default=1, on_delete=models.CASCADE)



