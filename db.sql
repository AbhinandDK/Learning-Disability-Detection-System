/*
SQLyog Community v13.1.5  (64 bit)
MySQL - 5.6.12-log : Database - learning disability detection
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`learning disability detection` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `learning disability detection`;

/*Data for the table `auth_group` */

/*Data for the table `auth_group_permissions` */

/*Data for the table `auth_permission` */

insert  into `auth_permission`(`id`,`name`,`content_type_id`,`codename`) values 
(1,'Can add log entry',1,'add_logentry'),
(2,'Can change log entry',1,'change_logentry'),
(3,'Can delete log entry',1,'delete_logentry'),
(4,'Can add permission',2,'add_permission'),
(5,'Can change permission',2,'change_permission'),
(6,'Can delete permission',2,'delete_permission'),
(7,'Can add group',3,'add_group'),
(8,'Can change group',3,'change_group'),
(9,'Can delete group',3,'delete_group'),
(10,'Can add user',4,'add_user'),
(11,'Can change user',4,'change_user'),
(12,'Can delete user',4,'delete_user'),
(13,'Can add content type',5,'add_contenttype'),
(14,'Can change content type',5,'change_contenttype'),
(15,'Can delete content type',5,'delete_contenttype'),
(16,'Can add session',6,'add_session'),
(17,'Can change session',6,'change_session'),
(18,'Can delete session',6,'delete_session'),
(19,'Can add equation',7,'add_equation'),
(20,'Can change equation',7,'change_equation'),
(21,'Can delete equation',7,'delete_equation'),
(22,'Can add exam',8,'add_exam'),
(23,'Can change exam',8,'change_exam'),
(24,'Can delete exam',8,'delete_exam'),
(25,'Can add feedback',9,'add_feedback'),
(26,'Can change feedback',9,'change_feedback'),
(27,'Can delete feedback',9,'delete_feedback'),
(28,'Can add login',10,'add_login'),
(29,'Can change login',10,'change_login'),
(30,'Can delete login',10,'delete_login'),
(31,'Can add objectss',11,'add_objectss'),
(32,'Can change objectss',11,'change_objectss'),
(33,'Can delete objectss',11,'delete_objectss'),
(34,'Can add user',12,'add_user'),
(35,'Can change user',12,'change_user'),
(36,'Can delete user',12,'delete_user');

/*Data for the table `auth_user` */

/*Data for the table `auth_user_groups` */

/*Data for the table `auth_user_user_permissions` */

/*Data for the table `django_admin_log` */

/*Data for the table `django_content_type` */

insert  into `django_content_type`(`id`,`app_label`,`model`) values 
(1,'admin','logentry'),
(3,'auth','group'),
(2,'auth','permission'),
(4,'auth','user'),
(5,'contenttypes','contenttype'),
(7,'Ldd_app','equation'),
(8,'Ldd_app','exam'),
(9,'Ldd_app','feedback'),
(10,'Ldd_app','login'),
(11,'Ldd_app','objectss'),
(12,'Ldd_app','user'),
(6,'sessions','session');

/*Data for the table `django_migrations` */

insert  into `django_migrations`(`id`,`app`,`name`,`applied`) values 
(1,'Ldd_app','0001_initial','2025-01-28 05:32:32.016586'),
(2,'contenttypes','0001_initial','2025-01-28 05:32:32.063456'),
(3,'auth','0001_initial','2025-01-28 05:32:32.422798'),
(4,'admin','0001_initial','2025-01-28 05:32:32.516935'),
(5,'admin','0002_logentry_remove_auto_add','2025-01-28 05:32:32.516935'),
(6,'contenttypes','0002_remove_content_type_name','2025-01-28 05:32:32.595056'),
(7,'auth','0002_alter_permission_name_max_length','2025-01-28 05:32:32.626306'),
(8,'auth','0003_alter_user_email_max_length','2025-01-28 05:32:32.657554'),
(9,'auth','0004_alter_user_username_opts','2025-01-28 05:32:32.673173'),
(10,'auth','0005_alter_user_last_login_null','2025-01-28 05:32:32.704420'),
(11,'auth','0006_require_contenttypes_0002','2025-01-28 05:32:32.704420'),
(12,'auth','0007_alter_validators_add_error_messages','2025-01-28 05:32:32.720042'),
(13,'auth','0008_alter_user_username_max_length','2025-01-28 05:32:32.751288'),
(14,'auth','0009_alter_user_last_name_max_length','2025-01-28 05:32:32.782535'),
(15,'sessions','0001_initial','2025-01-28 05:32:32.813783');

/*Data for the table `django_session` */

insert  into `django_session`(`session_key`,`session_data`,`expire_date`) values 
('zc1e9u8557qbmmqj7pfd5420rotp1ao2','NDMxNjZhZTkxYjQ0MjI3MTgwYjkyYmRjNmI3Nzk5OTIwYzQ4OWUwNjp7ImxpZCI6MX0=','2025-02-11 08:34:13.921239');

/*Data for the table `ldd_app_equation` */

insert  into `ldd_app_equation`(`id`,`equation`,`option_A`,`option_B`,`option_C`,`option_D`,`correct_answer`) values 
(1,'/static/images/20250128_062915.jpg','20','38','28','20','38'),
(2,'/static/images/20250128_063520.jpg','13','11','10','15','15'),
(3,'/static/images/20250128_063746.jpg','3','10','17','20','3');

/*Data for the table `ldd_app_exam` */

insert  into `ldd_app_exam`(`id`,`date`,`results`,`category`,`USER_id`) values 
(1,'2025-01-28','2','Discalculia Test',1);

/*Data for the table `ldd_app_feedback` */

insert  into `ldd_app_feedback`(`id`,`date`,`feedback`,`USER_id`) values 
(1,'263','nk',1),
(2,'2025-01-12 12:18:43.084423','good',1),
(3,'2025-01-12 12:19:22.212548','nice',1),
(4,'2025-01-15 11:19:05.133026','goodw',1);

/*Data for the table `ldd_app_login` */

insert  into `ldd_app_login`(`id`,`username`,`password`,`usertype`) values 
(1,'admin@gmail.com','admin','admin'),
(2,'user','user','user'),
(3,'achuaswin486@gmail.com','Aswin@2003','user'),
(4,'achuaswin486@gmail.com','Aswin@2003','user'),
(5,'ajay@gmail.com','aj12','user'),
(6,'ajay@gmail.com','aj12','user'),
(7,'b','12','user'),
(8,'kuku@123gmail.com','kuku@123','user');

/*Data for the table `ldd_app_objectss` */

insert  into `ldd_app_objectss`(`id`,`name`,`image`) values 
(9,'aaa','/static/images/20250109140045.jpg');

/*Data for the table `ldd_app_user` */

insert  into `ldd_app_user`(`id`,`name`,`age`,`email`,`phone`,`image`,`LOGIN_id`) values 
(1,'u','55','5858','952','nb',2),
(2,'aswin','21','achuaswin486@gmail.com','9778126668','',1),
(3,'aswin','21','achuaswin486@gmail.com','9778126668','',1),
(4,'ajay','21','ajay@gmail.com','+917025983139','/static/images/15012025-130942.png',5),
(5,'ajay','21','ajay@gmail.com','+917025983139','/static/images/15012025-130944.png',6),
(6,'bb','99','b','996','/static/images/15012025-131111.png',7),
(7,'kuku','21','kuku@123gmail.com','3698527410','/static/images/16012025-104936.png',8);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
