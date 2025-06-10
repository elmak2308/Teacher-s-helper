role = "Учитель"
ask = ""
direct = ""
file_name = ""
title = ""
level_of_creative = float()


folder_id = 'ИД СЕРВЕРНОГО БОТА'
try:
    iam_token = open('/home/dan/.iam_token').read().strip() # папка другая
except IOError as e:
    raise Exception(f'IAM_TOKEN not found with {e}')