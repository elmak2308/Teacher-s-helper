# Получение из запроса пользователя (или указать значения по цумолчанию)
role = "Учитель" # Роль пользователя
ask = "Создай тест по физике на 15 вопросов" # Запрос к ИИ
direct = "ВАША ПАПКА" # Место сохранения файла
file_name = "Ответ.json" # Название файла
title = "Тест" # Заголовок
level_of_creative = float(0.1) #Можно добавить ползунок

#!/bin/bash (Вводится в консоль)
"""
export FOLDER_ID=xxxxxxxxxxxxxxxxxxx
yc iam create-token > ~/.iam_token
"""

folder_id = 'b1goanfad3n1392h6v76' # Не меняется (ID серверного бота, отправляющего запросы)
try:
    iam_token = server.get_iam_token.strip() # Сослаться на файл или запустить это здесь (запрос токена через консоль)
except IOError as e:
    print("DEBUG: YOU CAN'T GET IAM-TOKEN, PLEASE TRY AGAIN")
    raise Exception(f'IAM_TOKEN not found with {e}')