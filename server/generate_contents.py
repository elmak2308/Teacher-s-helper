#!/usr/bin/env python3

import argparse

from requests import post
from textwrap import fill

parser = argparse.ArgumentParser()

# Ввод информации изменить
ARGS = [
    ('-r', '--role',        str,   'Помощник учителя', 'Role of generator'), # Получать роль пользователя
    ('-a', '--ask',         str,   'Что такое осень',      'Text of question'), # В основном это
    ('-f', '--file',        str,   None,                   'File with question'),
    ('-t', '--temperature', float, 0.1,                    '"Temperature" of model'), # Настраивать уникальность в программе
    ('-w', '--wrap-at',     int,   None,                   'Wrap at this symbol'),
    ('-i', '--ident',       int,   None,                   'Ident spaces'),
]

# y = x['result']['alternatives'][0]['message']['text']
# z = y.replace('**','').replace('`','')
# print('=====================================================')
# Z = z.split('\n\n')
# for x in Z:
#     if len(x) > 60:
#         print(fill(x),'\n')
#     else:
#         print(x)
# print('=====================================================')

def parse_args(parser, args):
    for a_short, a_long, a_type, a_value, a_help in args:
        parser.add_argument(a_short, a_long, type=a_type, default=a_value, help=a_help)

    args = parser.parse_args()
    print(args)

    role = args.role
    ask = args.ask
    file = args.file
    temperature = args.temperature
    wrap_at = args.wrap_at
    try:
        ident = ' ' * args.ident
    except TypeError:
        ident = args.ident
        
    try:
        ask = open(file).read() if file is not None else ask
    except IOError as e:
        raise Exception(f'File {file} asked but {e}')

    return role, ask, temperature, wrap_at, ident


def parse_yc():
    folder_id = 'b1g16h2dmk115b1v1ujh' # фолдер другой
    try:
        iam_token = open('/home/dan/.iam_token').read().strip() # папка другая
    except IOError as e:
        raise Exception(f'IAM_TOKEN not found with {e}')
    return iam_token, folder_id


def create_data(role, ask, temperature, folder_id):
    data = {
      "modelUri": f"gpt://{folder_id}/yandexgpt-lite",
      "modelUri": f"gpt://{folder_id}/yandexgpt/rc",
      "completionOptions": {
        "stream": False,
        "temperature": temperature,
        "maxTokens": "5000"
      },
      "messages": [
        {
          "role": "system",
          "text": role
        },
        {
          "role": "user",
          "text": ask
        }
      ]
    }
    return data


def post_to_yc(data, iam_token, folder_id):
    x = post(
        "https://llm.api.cloud.yandex.net/foundationModels/v1/completion",
        json=data, 
        headers={
            "Content-Type": "application/json",
            "Authorization": f"Bearer { iam_token }",
            "x-folder-id": f"{ folder_id }" ,
        },
        timeout=50,
    ).json()

    return x


def parse_rez(rez, wrap_at, ident):

    alternatives = rez['result']['alternatives']
    text = ""
    for alternative in alternatives:
        _in = alternative['message']['text']
        _in = _in.replace(':**', '.').replace('**', '')
        text += _in

    if wrap_at is None or ident is None:
        out = text
    else:
        out = ''
        for p in text.split('\n\n'):
            pp = fill(text=p, width=wrap_at)
            for l in pp.split('\n'):
                out += f'{ident}{l}\n'
            out += '\n'

    return f'==============\n\n\n{out}\n\n\n=============='


if __name__ == '__main__':
    role, ask, temperature, wrap_at, ident = parse_args(argparse.ArgumentParser(), ARGS)
    iam_token, folder_id = parse_yc()
    data = create_data(role, ask, temperature, folder_id)
    rez = post_to_yc(data, iam_token, folder_id)
    to_print = parse_rez(rez, wrap_at, ident)
    print(to_print) # Замена на вывод файла в директорию