#!/usr/bin/env python3

import argparse
import re
import sys

from requests import post

parser = argparse.ArgumentParser()

ARGS = [
    ('-f', '--input-file', str, '/tmp/x',           'File to read'),
    ('-a', '--ask',        str, 'Что такое осень?', 'Text to retell'),
]


def parse_args(parser, args):
    for a_short, a_long, a_type, a_value, a_help in args:
        parser.add_argument(a_short, a_long, type=a_type, default=a_value, help=a_help)

    args = parser.parse_args()

    input_file = args.input_file

    try:
        ask = open(input_file).read()
    except FileNotFoundError:
        ask = args.ask

    if ask == 'Что такое осень?':
        raise ValueError('No text provided')

    return ask


class YC():

    def __init__(self):
        self.folder_id = 'b1g16h2dmk115b1v1ujh'
        try:
            self.iam_token = open('/home/dan/.iam_token').read().strip()
        except IOError as e:
            raise Exception(f'IAM_TOKEN not found with {e}')
        

    def __create_data(self, role, ask):
        data = {
          "modelUri": f"gpt://{self.folder_id}/yandexgpt/rc",
          "completionOptions": {
            "stream": False,
            "temperature": "0.1",
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


    def __post_to_yc(self, data):
        x = post(
            "https://llm.api.cloud.yandex.net/foundationModels/v1/completion",
            json=data, 
            headers={
                "Content-Type": "application/json",
                "Authorization": f"Bearer { self.iam_token }",
                "x-folder-id": f"{ self.folder_id }" ,
            },
            timeout=50,
        ).json()

        return x


    def __parse_rez(self, rez):

        alternatives = rez['result']['alternatives']
        text = ""
        for alternative in alternatives:
            _in = alternative['message']['text']
            _in = _in.replace(':**', '.').replace('**', '')
            text += _in

        text = re.sub(r'^\* ', '- ', text)
        text = re.sub(r'\n(\d+)\.\s', '\n\\1) ', '\n'+text)
        text = text.replace('\n\n','\n')

        return text


    def __call__(self, role, ask):
        x, y, z = None, None, None
        try:
            x = self.__create_data(role, ask)
            y = self.__post_to_yc(x)
            z = self.__parse_rez(y)
        except Exception as e:
            print(role, file=sys.stderr)
            print(ask, file=sys.stderr)
            print(x, file=sys.stderr)
            print(y, file=sys.stderr)
            print(z, file=sys.stderr)
            raise e
        return z


if __name__ == '__main__':
    generate_text = YC()
    ask = parse_args(argparse.ArgumentParser(), ARGS)
    out = generate_text('Перескажи в научно-техническом стиле', ask)
    print("=============")
    print(out)
    print("=============")
    