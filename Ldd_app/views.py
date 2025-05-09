import datetime
import random

from django.core.files.storage import FileSystemStorage
from django.http import HttpResponse, JsonResponse
from django.shortcuts import render
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split

from Ldd_app.models import *

static_path=r"C:\Users\abhin\PycharmProjects\Learning_Disability_Detection\Ldd_app\static\\"
def admin_home(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    request.session['head']="ADMIN HOME"
    # return render(request,'admin/index2.html')
    return render(request,'admin/index_new.html')

def log(request):
    return render(request,'index.html')

def log_post(request):
    un=request.POST['name']
    PS=request.POST['password']
    res=login.objects.filter(username=un,password=PS)
    if res.exists():
        res=res[0]
        request.session['lid']=res.id
        if res.usertype=="admin":
            return HttpResponse("<script>alert('LOGIN SUCCESS');window.location='/admin_home'</script>")
        else:
            return HttpResponse("<script>alert('NO ACCESS');window.location='/'</script>")

    else:
        return HttpResponse("<script>alert('INVALID DETAILS');window.location='/'</script>")


def changepassword(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    request.session['head'] = "CHANGE PASSWORD"
    return render(request,'admin/change password.html')

def changepassword_post(request):
    cp=request.POST['textfield']
    np = request.POST['textfield2']
    cfp = request.POST['textfield3']
    res=login.objects.filter(password=cp,id=request.session['lid'])
    if res.exists():
        if np == cfp:
            login.objects.filter(id=request.session['lid']).update(password=np)
            return HttpResponse("<script>alert('password successfully changed');window.location='/'</script>")
        return HttpResponse("<script>alert('password mismatch');window.location='/changepassword'</script>")

    return  HttpResponse("<script>alert('pasword not found');window.location='/changepassword'</script>")


def Add_obj(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    request.session['head'] = "ADD OBJECT"
    return render(request,'admin/Add obj.html')

def Add_obj_post(request):
    n=request.POST['textfield']
    p=request.FILES['fileField']
    data=objectss.objects.filter(name=n)
    if data.exists():
        return HttpResponse("<script>alert('already exist');window.location='/Add_obj#a'</script>")
    else:

        d=datetime.datetime.now().strftime("%Y%m%d%H%M%S")
        fs=FileSystemStorage()
        fs.save(static_path + "images\\"+d+'.jpg',p)
        path="/static/images/"+d+'.jpg'
        obj=objectss()
        obj.image=path
        obj.name=n
        obj.save()
        return HttpResponse("<script>alert('object added');window.location='/view_obj#a'</script>")

def view_obj(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    request.session['head'] = "VIEW OBJECT"
    res=objectss.objects.all()
    return render(request,'admin/View Obj.html',{"data":res})

def delete_obj(request,id):
    objectss.objects.get(id=id).delete()
    return HttpResponse("<script>alert('object deleted');window.location='/view_obj#a'</script>")

def eqn_add(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    request.session['head'] = "ADD EQUATION"
    return render(request,'admin/addd_eqn.html')

def eqn_add_post(request):
    eq=request.FILES['fileField']
    corr=request.POST['textfield2']
    option_A=request.POST['textfield3']
    option_B=request.POST['textfield4']
    option_C=request.POST['textfield5']
    option_D=request.POST['textfield6']

    d=datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
    fs=FileSystemStorage()
    fs.save(static_path + "images\\" + d + ".jpg", eq)
    path="/static/images/" + d + ".jpg"


    eqq=equation()
    eqq.equation=path
    eqq.correct_answer=corr
    eqq.option_A=option_A
    eqq.option_B=option_B
    eqq.option_C=option_C
    eqq.option_D=option_D
    eqq.save()
    return HttpResponse("<script>alert('equation added');window.location='/view_eqn#a'</script>")



def view_eqn(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    aw=equation.objects.all()
    request.session['head'] = "VIEW EQUATIONS"
    return render(request,'admin/view_eqn.html',{"abc":aw})

def delete_eqn(request,id):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    equation.objects.get(id=id).delete()
    return HttpResponse("<script>alert('equation  deleted');window.location='/view_eqn#a'</script>")


def view_user(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    aq=user.objects.all()
    request.session['head'] = "VIEW USERS"
    return render(request,'admin/View User.html',{"pqr":aq})


def view_feed(request):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    ae=feedback.objects.all()
    request.session['head'] = "VIEW FEEDBACK"
    return render(request,'admin/View Feed.html',{"rst":ae})

def test_history(request, id):
    if request.session['lid']=="":
        return HttpResponse("<script>alert('Your session ended.');window.location='/'</script>")
    at=exam.objects.filter(USER_id=id).order_by("-id")
    request.session['head'] = "TEST HISTORY"
    return render(request,'admin/test history.html',{"wxy":at})

def logout(request):
    request.session['lid']=""
    return HttpResponse("<script>alert('  log out success');window.location='/'</script>")



#######################################################################################################3
def user_login(request):
    username = request.POST['username']
    password = request.POST['password']
    res=login.objects.filter(username=username,password=password,usertype="user")
    if res.exists():
        res=res[0]
        print(res)
        return JsonResponse({"status":"ok","lid":res.id})
    else:
        return JsonResponse({"status":"no"})


def user_register(request):
    name = request.POST['name']
    email = request.POST['email']
    phone = request.POST['phone']
    age =request.POST['age']
    passw = request.POST['password']
    confirmpass = request.POST['confirmpassword']

    img = request.FILES['pic']
    d=datetime.datetime.now().strftime("%d%m%Y-%H%M%S")
    fs=FileSystemStorage()
    fs.save(static_path + "images\\"+d+'.png',img)
    path="/static/images/"+d+'.png'

    if passw==confirmpass:

        obj1 = login()
        obj1.usertype='user'
        obj1.username=email
        obj1.password=confirmpass
        obj1.save()





        obj=user()
        obj.name=name
        obj.email=email
        obj.age=age
        obj.phone=phone
        obj.image=path
        obj.LOGIN=obj1
        obj.save()

        return JsonResponse({"status": "ok"})
    else:
        return JsonResponse({"status": "no"})


def change_password(request):
    lid=request.POST['lid']
    currentpass = request.POST['oldpass']
    newpass = request.POST['newpass']
    confpass = request.POST['confirmpass']
    res=login.objects.filter(password=currentpass, id=lid)
    if res.exists():
        if newpass == confpass:
            login.objects.filter(id=lid).update(password=newpass)
            return JsonResponse({"status":"ok"})
    return JsonResponse({"status": "no"})

def user_feedback(request):
    lid=request.POST['lid']
    print(lid)
    feed = request.POST['feedback']
    uid=user.objects.get(LOGIN=lid).id

    obj2=feedback()
    obj2.feedback=feed
    obj2.date=datetime.datetime.now()
    obj2.USER_id=uid
    obj2.save()

    return JsonResponse({"status": "ok"})

def load_discalculia_question(request):
    qn_cnt=request.POST['qn_cnt']
    cnt=int(qn_cnt)
    res=equation.objects.all()
    ln=len(res)
    print("LL ", ln)
    if res.exists():
        res=res[cnt]
        print(res)
        return JsonResponse({"status":"ok", "equation":res.equation, "option_A":res.option_A, "option_B":res.option_B,
                             "option_C":res.option_C, "option_D":res.option_D, "correct_answer":res.correct_answer, "ln":ln})
    else:
        return JsonResponse({"status":"no"})

def submit_score(request):
    lid=request.POST['lid']
    correct=request.POST['correct']
    attended=request.POST['attended']
    scr = int(correct) * 100 / int(attended)
    scr=round(scr, 2)
    res = exam.objects.filter(USER__LOGIN_id=lid, date=datetime.datetime.now().date(), category="Discalculia Test")
    if res.exists():
        res = res[0]
        res.results = scr
        res.save()
    else:
        obj = exam()
        obj.date = datetime.datetime.now().date()
        obj.results = scr
        obj.category = "Discalculia Test"
        obj.USER = user.objects.get(LOGIN_id=lid)
        obj.save()
    return JsonResponse({"status": "ok"})

def and_view_objects(request):
    res=objectss.objects.all()
    ar=[]
    for i in res:
        ar.append({
            "name":i.name, "image":i.image
        })
    return JsonResponse({"status": "ok", "data":ar})

def and_check_ans(request):
    pos=request.POST['pos']
    ans=request.POST['ans']
    res=objectss.objects.all()
    a= res[int(pos)]
    if a.name.lower() == ans.lower():
        return JsonResponse({"status": "ok"})
    else:
        return JsonResponse({"status": "no"})

def and_insert_speech_marks(request):
    lid=request.POST['lid']
    correct=request.POST['correct']
    attended=request.POST['attended']
    scr=int(correct)*100 / int(attended)
    res = exam.objects.filter(USER__LOGIN_id=lid, date=datetime.datetime.now().date(), category="Dyslexia Test")
    if res.exists():
        res = res[0]
        res.results = scr
        res.save()
    else:
        obj = exam()
        obj.date = datetime.datetime.now().date()
        obj.results = scr
        obj.category = "Dyslexia Test"
        obj.USER = user.objects.get(LOGIN_id=lid)
        obj.save()
    return JsonResponse({"status": "ok"})


def scramble_word(word):
    # Convert the word to a list of characters
    word_list = list(word)
    # Shuffle the list
    random.shuffle(word_list)
    # Convert the list back to a string
    scrambled_word = ''.join(word_list)
    return scrambled_word

#   scrambled
def and_view_objects_scr(request):
    res=objectss.objects.all()
    ar=[]
    for i in res:
        r=scramble_word(i.name)
        while r == i.name:
            r = scramble_word(i.name)
        ar.append({
            "name":i.name, "image":i.image, "scr":r
        })
    return JsonResponse({"status": "ok", "data":ar})



def and_insert_scr_marks(request):
    lid=request.POST['lid']
    correct=request.POST['correct']
    attended=request.POST['attended']
    scr=int(correct)*100 / int(attended)
    res = exam.objects.filter(USER__LOGIN_id=lid, date=datetime.datetime.now().date(), category="Dyslexia Test")
    if res.exists():
        res = res[0]
        res.results = scr
        res.save()
    else:
        obj = exam()
        obj.date = datetime.datetime.now().date()
        obj.results = scr
        obj.category = "Dyslexia Test"
        obj.USER = user.objects.get(LOGIN_id=lid)
        obj.save()
    return JsonResponse({"status": "ok"})


def user_suggestions(request):
    return JsonResponse({"status": "ok"})

import pandas as pd
import joblib
data = pd.read_csv(r"C:\Users\abhin\PycharmProjects\Learning_Disability_Detection\Ldd_app\static\dataset.csv")
x = data.values[1:, :3]
y = data.values[1:, 3]
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2)
rf = RandomForestClassifier()
rf.fit(x_train, y_train)
joblib.dump(rf, r'C:\Users\abhin\PycharmProjects\Learning_Disability_Detection\Ldd_app\static\rf_model.joblib')


def user_view_result(request):
    lid=request.POST['lid']
    res_dysl = exam.objects.filter(USER__LOGIN_id=lid, category="Dyslexia Test")
    res_adhd = exam.objects.filter(USER__LOGIN_id=lid, category="ADHD Test")
    res_dysc = exam.objects.filter(USER__LOGIN_id=lid, category="Discalculia Test")

    tot_dysl=0
    for i in res_dysl:
        tot_dysl += float(i.results)
    if len(res_dysl) == 0:
        avg_dysl = 100
    else:
        avg_dysl=float(tot_dysl) /len(res_dysl)

    tot_adhd=0
    for i in res_adhd:
        tot_adhd += float(i.results)
    if len(res_adhd) == 0:
        avg_adhd = 0.0
    else:
        avg_adhd=float(tot_adhd) /len(res_adhd)

    tot_dysc=0
    for i in res_dysc:
        tot_dysc += float(i.results)
    if len(res_dysc) == 0:
        avg_dysc = 100
    else:
        avg_dysc=float(tot_dysc) /len(res_dysc)
    if avg_dysl > 20 and avg_adhd < 30 and avg_dysc >20:
        return JsonResponse({"status": "healthy"})

    # feats=[avg_dysl, avg_adhd, avg_dysc]
    feats=[avg_dysc, avg_dysl, avg_adhd]
    print(feats)
    rf=joblib.load(r'C:\Users\abhin\PycharmProjects\Learning_Disability_Detection\Ldd_app\static\rf_model.joblib')
    y_pred=rf.predict(x_test)
    acc=accuracy_score(y_test, y_pred)
    acc=round(acc*100, 2)
    # print("Accuracy : ", acc)
    pred=rf.predict([feats])
    print("Prediction : ", pred[0])
    res=""
    if pred[0] == 0:
        res="Dyscalculia"
    elif pred[0] == 2:
        res="ADHD"
    elif pred[0] == 1:
        res="Dyslexia"

    sugg={
        "Dyscalculia" : "Exercise self-talking through solving problems.\nConnect math to real life with concrete examples.\nReview what you have learned before learning new skills.",
        "ADHD" : "Sit in the front of class to limit distractions\nTurn off your phone when doing homework\nLearn to meditate.",
        "Dyslexia" : "Emphasize instant letter recognition whenever possible.\nBuild background knowledge and set a purpose for reading to strengthen your comprehension."
                     "\nWhen possible, reread passages after the teacher reads the passage for you.",
    }
    return JsonResponse({"status":"ok", "pred":res, "sugg":sugg[res]})



def stud_cam_start(request):
    from Ldd_app.distraction_detection import cam_start
    cam_start()
    return JsonResponse({"status":"ok"})

def stud_cam_stop(request):
    from Ldd_app.distraction_detection import cam_close
    cam_close()
    return JsonResponse({"status":"ok"})






