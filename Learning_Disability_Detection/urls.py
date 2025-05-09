"""Learning_Disability_Detection URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include

from Ldd_app import views

urlpatterns = [
    # path('admin/', admin.site.urls),
    path('', views.log),
    path('log_post', views.log_post),
    path('changepassword', views.changepassword),
    path('changepassword_post', views.changepassword_post),
    path('Add_obj', views.Add_obj),
    path('Add_obj_post', views.Add_obj_post),
    path('view_obj', views.view_obj),
    path('delete_obj/<id>', views.delete_obj),
    path('eqn_add', views.eqn_add),
    path('eqn_add_post', views.eqn_add_post),
    path('view_eqn', views.view_eqn),
    path('delete_eqn/<id>', views.delete_eqn),
    path('view_user', views.view_user),
    path('view_feed', views.view_feed),
    path('test_history/<id>', views.test_history),
    path('admin_home', views.admin_home),
    path('logout',views.logout),
    #################################################################3
    path('user_login',views.user_login),
    path('user_register',views.user_register),
    path('change_password',views.change_password),
    path('user_feedback',views.user_feedback),
    path('user_suggestions',views.user_suggestions),
    path('load_discalculia_question',views.load_discalculia_question),
    path('submit_score',views.submit_score),
    path('and_view_objects',views.and_view_objects),
    path('and_check_ans',views.and_check_ans),
    path('and_insert_speech_marks',views.and_insert_speech_marks),
    path('and_view_objects_scr',views.and_view_objects_scr),
    path('and_insert_scr_marks',views.and_insert_scr_marks),
    path('user_view_result',views.user_view_result),
    path('stud_cam_start',views.stud_cam_start),
    path('stud_cam_stop',views.stud_cam_stop),
]
