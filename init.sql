CREATE DATABASE IF NOT EXISTS leafpan;

创建表 users
id 雪花算法生成的唯一标识符
username 用户名
password_hash 密码哈希值
email 邮箱
created_at 创建时间
updated_at 更新时间
启用禁用
avatar_url 头像URL
网盘总容量 单位MB
已用容量 单位MB

用户文件对应表
id 雪花算法生成的唯一标识符
user_id 用户ID
file_id 文件ID
file_path 文件路径
created_at 创建时间
updated_at 更新时间

文件表
id 雪花算法生成的唯一标识符
file_name 文件名称
file_size 文件大小 单位MB
file_type 文件类型
created_at 创建时间
updated_at 更新时间
