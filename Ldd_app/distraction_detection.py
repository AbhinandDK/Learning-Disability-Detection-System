import os
from datetime import datetime

import cv2
import dlib
import face_recognition
import numpy as np
from PIL import Image
from scipy.spatial import distance as dist

from Ldd_app.DBConnection import Db
from Ldd_app.mtcnn.mtcnn import MTCNN

static_path = "C:/Users/abhin/PycharmProjects/Learning_Disability_Detection/Ldd_app/static/"
temp_image_path = "C:/Users/abhin/PycharmProjects/Learning_Disability_Detection/Ldd_app/static/temp_image.jpg"
detector = MTCNN() #Very good yaw and pitch angles
detector_2 = dlib.get_frontal_face_detector() #Blink/sleep detection
predictor_2 = dlib.shape_predictor(static_path + 'models/shape_predictor_68_face_landmarks.dat')

font = cv2.FONT_HERSHEY_COMPLEX
font_size= 0.4
blue= (225,0,0)
red = (0,0,255)
yellow = (0,255,255)
orange = (0,155,255)
green=(0,128,0)
green_2 = (0, 255, 0)

#Eye Parameters
RIGHT_EYE_POINTS = list(range(36, 42))
LEFT_EYE_POINTS = list(range(42, 48))
EYE_AR_RATIO = 0.22
EYE_AR_CONSEC_FRAMES = 1
SLEEP_FRAMES = 5

#Eye counter Variables
counter = 0
total = 0

#Calculates eye aspect ratios
def eye_aspect_ratio(frame, eye):
    #Compute euclidean distance between the vertical eye landmarks
    A = dist.euclidean(eye[1], eye[5])
    B = dist.euclidean(eye[2], eye[4])
    #Compute euclidean distance between horizontal eye landmarks
    C = dist.euclidean(eye[0], eye[3])
    ear = (A + B)/(2 * C)
    return ear

#Draw eye contours
def draw_eye_contours(frame, rects):
    for rect in rects:
        #get facial landmarks
        landmarks = np.matrix([[p.x, p.y] for p in predictor_2(frame, rect).parts()])
        #get left eye landmarks
        left_eye = landmarks[LEFT_EYE_POINTS]
        #get right eye landmarks
        right_eye = landmarks[RIGHT_EYE_POINTS]
        #draw contours on the eyes
        left_eye_hull = cv2.convexHull(left_eye)
        right_eye_hull = cv2.convexHull(right_eye)
        cv2.drawContours(frame, [left_eye_hull], -1, green_2, 1)
        cv2.drawContours(frame, [right_eye_hull], -1, green_2, 1)
        return left_eye, right_eye


#Detect face and prepare data
def face_points(frame,bbs,points):
    bb = bbs[0]
    keypoints = points[:,0]
    return bb, keypoints

#Draw rectangle and facial landmarks
def draw(frame,bb,keypoints):

    cv2.rectangle(frame, (int(bb[0]),int(bb[1])), (int(bb[2]), int(bb[3])), orange, 2)

    cv2.circle(frame, (keypoints[2], keypoints[7]),2,yellow,2) #Nose
    cv2.circle(frame, (keypoints[3], keypoints[8]),2,yellow,2) #Left Mouth
    cv2.circle(frame, (keypoints[4], keypoints[9]),2,yellow,2) #Right Mouth

def apply_offsets(frame, bb):
    x1 = int(bb[0])
    y1 = int(bb[1])
    x2 = int(bb[2])
    y2 = int(bb[3])
    return x1,x2,y1,y2

#Change of lengths between each eye wrt nose
def yaw(pts):
    l_eye_nose = pts[2] - pts[0]
    r_eye_nose = pts[1] - pts[2]
    diff_eyes_nose = l_eye_nose - r_eye_nose
    return diff_eyes_nose

#Change of lengths between eyes and mouth wrt nose
def pitch(pts):
    avg_eye_y = (pts[5] + pts[6])/2
    avg_mouth_y = (pts[8] + pts[9])/2
    e2n = avg_eye_y - pts[7]
    n2m = pts[7] - avg_mouth_y
    y_diff = e2n - n2m
    return y_diff

#Creates a list of the desired variables
# def dataframe(yaw, pitch, label, label_2, timestamp):
#     yaw_angle.append(yaw)
#     pitch_angle.append(pitch)
#     yaw_label.append(label)
#     pitch_label.append(label_2)
#
#     Time_stamp.append(timestamp)
#     return Time_stamp, yaw_angle, pitch_angle, yaw_label, pitch_label


video_save = True

fps = 10
video_format = cv2.VideoWriter_fourcc('M','J','P','G')
video_max_frame = 10
video_outs=[]

dist_list=[]
full_list=[]
scr=0
stid=0

#Start Video Camera Feed

cap = cv2.VideoCapture(0)       #   lap camera
# cap = cv2.VideoCapture(1)       #   web  camera





def mark_distraction(student_id, score):

    db=Db()
    res=db.selectOne("SELECT * FROM `ldd_app_exam` WHERE USER_id='"+str(student_id)+"' AND category='ADHD Test' AND DATE=CURDATE()")
    if res is None:
        db=Db()
        db.insert(
            "INSERT INTO `ldd_app_exam` VALUES(NULL, CURDATE(), '"+str(score)+"', 'ADHD Test', '"+str(student_id)+"')"
        )
    else:
        db=Db()
        db.update("update ldd_app_exam set results='"+str(score)+"' where id='"+str(res['id'])+"'")
    # print(f"Distraction marked")



def distract_log():
    # code for face recognition
    db=Db()

    students = db.select("SELECT * FROM `ldd_app_user`")
    if not students:
        print("No users registered in the database.")
        return

    known_faces = []
    user_ids = []
    person_names = []

    for student in students:

        pic = student["image"]
        img_path = os.path.join(static_path, "images", os.path.basename(pic))

        if os.path.exists(img_path):
            student_image = np.array(Image.open(img_path))
            encodings = face_recognition.face_encodings(student_image)

            if encodings:
                known_faces.append(encodings[0])
                user_ids.append(student["id"])
                person_names.append(student["name"])
            else:
                print(f"Warning: No face encodings found for {student['name']}.")
        else:
            print(f"Error: Image not found for {student['name']} at {img_path}.")

    captured_image = np.array(Image.open(temp_image_path))
    unknown_encodings = face_recognition.face_encodings(captured_image)
    print("Faces ", len(unknown_encodings))

    if unknown_encodings:
        detected_valid_face = False  # Flag to track if a registered face is found

        for unknown_encoding in unknown_encodings:
            matches = face_recognition.compare_faces(known_faces, unknown_encoding, tolerance=0.45)
            if True in matches:
                matched_idx = matches.index(True)
                student_id = user_ids[matched_idx]
                student_name = person_names[matched_idx]



                detected_valid_face = True  # Mark that a valid face has been detected
                return student_id # Stop after processing one face match

        if not detected_valid_face:
            print("Unregistered face detected.")
            # Optionally, you can log or display an alert about unregistered faces.

    else:
        print("No faces detected in the current frame. Adjust the camera angle or lighting.")

    # The loop continues, attendance marking won't repeat for the same student
    print("Finished processing frame. Waiting for the next one...")


def cam_start():
    yaw_angle = []
    pitch_angle = []
    Time_stamp = []
    yaw_label = []
    pitch_label = []
    distraction_cnt = 0
    left_cnt = 0
    right_cnt = 0
    top_cnt = 0
    ret, frames = cap.read()
    frame = np.array(frames)
    frame = cv2.flip(frame, 1)
    timestamp = datetime.now()
    cv2.imwrite(temp_image_path, frame)
    stid = distract_log()
    while True:
        #Capture Face
        ret,frames = cap.read()
        frame = np.array(frames)
        frame = cv2.flip(frame,1)
        timestamp = datetime.now()
        cv2.imwrite(temp_image_path, frame)

        #Grayscale conversion
        gray_image = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        rects = detector_2(gray_image, 0)

        #Use MTCNN to detect faces
        bbs, points = detector.detect_faces(frame)



        if len(bbs) > 0:
            bb, keypoints = face_points(frame,bbs,points)




            #Head Rotation Labels
            if yaw(keypoints) > 15:
                label = 'Head Right'

                dist_list.append(1)
                full_list.append(1)
            elif yaw(keypoints) < -15:
                label = 'Head Left'

                dist_list.append(1)
                full_list.append(1)
            else:
                label = 'Facing camera'
                full_list.append(1)

            if pitch(keypoints) > 15:
                label_2 = 'Head up'

                dist_list.append(1)
                full_list.append(1)

            elif pitch(keypoints) < -10:
                label_2 = 'Head Down'

                full_list.append(1)
            else:
                label_2 = 'Facing camera'

                full_list.append(1)
            scr = len(dist_list) * 100 / len(full_list)
            scr = round(scr, 2)
            print(len(dist_list), "/", len(full_list), "   Scr:",scr, "   St",stid)

            if stid!=0 and stid is not None:
                mark_distraction(stid, scr)
                print(f"Distraction marked")
            cv2.putText(frame, 'Yaw : '+label, (10,140), font, font_size, blue, 1)
            cv2.putText(frame, 'Pitch : '+label_2, (10,160), font, font_size, red, 1)
            cv2.putText(frame, 'Time: '+str(timestamp.strftime("%Y-%m-%d %H:%M:%S")), (200,10), font, font_size, green, 1)

        cv2.imshow('Face_Detector',frame)
        if cv2.waitKey(1) &0xFF == ord('q'):
            break



def cam_close():

    cap.release()
    cv2.destroyAllWindows()
    exit()

