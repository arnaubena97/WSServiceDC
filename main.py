import requests
import sys


def get():
    mode = input("Do you want to search the exam by ID or Description? (I/D): ")
    resp = 'http://localhost:8080/Practica2Web/rest/text'
    atribute = input("Introduce a text to search: ")
    value = requests.post(url=resp, json=[mode, atribute])
    value = str(value.content)[2:-1]
    data_to_csv = value.split('?')

    print("\nID - DESCRIPTION - DATE - TIME - LOCATION")
    for i in data_to_csv:
        print(str(i)[:-8] + '\n')

    csv = input("Do you want to store the result to a csv file? (Y/N): ")
    if csv == 'Y':
        f = open('exam.csv', 'w')
        f.write('ID - DESCRIPTION - DATE - TIME - LOCATION\n')
        for i in data_to_csv:
            f.write(str(i)[:-8] + '\n')
        f.close()


def post():
    resp = 'http://localhost:8080/Practica2Web/rest/post'

    IDExam = '1' #input("Introduce the exam ID: ")
    Description = input("Introduce the description: ")
    Date = input("Introduce the date: ")
    Time = input("Introduce the time: ")
    Location = input("Introduce the location: ")

    r = requests.post(url=resp, json=[IDExam, Description, Date, Time, Location])
    print("\n" + str(r.content)[2:-1] + "\n")


def add_grades():
    resp = 'http://localhost:8080/Practica2Web/rest/addgrades'

    IDExam = input("Introduce the exam ID: ")
    IDStudent = input("Introduce the student ID: ")
    IDGrade = 1
    grade = input("Introduce the grade: ")

    r = requests.post(url=resp, json=[IDGrade, IDExam, IDStudent, grade])
    print(r.content)


def mod():
    resp = 'http://localhost:8080/Practica2Web/rest/mod'

    IDExam = input("Introduce the exam ID: ")
    Description = input("Introduce the description: ")
    Date = input("Introduce the date: ")
    Time = input("Introduce the time: ")
    Location = input("Introduce the location: ")

    requests.post(url=resp, json=[IDExam, Description, Date, Time, Location])


def modify_description():
    resp = 'http://localhost:8080/Practica2Web/rest/moddesc'

    IDExam = input("Introduce the exam ID: ")
    Description = input("Introduce the description: ")

    requests.post(url=resp, json=[IDExam, Description])


def delete():
    resp = 'http://localhost:8080/Practica2Web/rest/del'
    IDExam = input("Introduce the exam ID: ")
    requests.post(url=resp, json=[IDExam])


def validate(user):
    resp = 'http://localhost:8080/Practica2Web/rest/validate'
    value = requests.post(url=resp, data=user)

    if str(value.content)[2:-1] == 'false':
        print("You must register at the system")
        iduser = '1'
        profile = input("Introduce your pass: ")

        resp = 'http://localhost:8080/Practica2Web/rest/adduser'
        requests.post(url=resp, json=[iduser, user, profile])
        return profile
    else:
        return str(value.content)[2:-1]


def save_exams():
    resp = 'http://localhost:8080/Practica2Web/rest/storeexams'
    value = requests.post(url=resp, data='')
    value = str(value.content)[2:-1]
    data_to_csv = value.split('?')

    print('ID - DESCRIPTION - DATE - TIME - LOCATION\n')
    for i in data_to_csv:
        print(str(i)[:-8] + '\n')

    csv = input("Do you want to store the result to a csv file? (Y/N): ")
    if csv == 'Y':
        f = open('exams.csv', 'w')
        f.write('ID - DESCRIPTION - DATE - TIME - LOCATION\n')
        for i in data_to_csv:
            f.write(str(i)[:-8] + '\n')
        f.close()


def save_grades():
    resp = 'http://localhost:8080/Practica2Web/rest/storegrade'
    value = requests.post(url=resp, data='')
    value = str(value.content)[2:-1]
    data_to_csv = value.split('?')

    for i in data_to_csv:
        print(i)

    f = open('grades.csv', 'w')
    for i in data_to_csv:
        f.write(i + '\n')
    f.close()


if __name__ == "__main__":
    user = input("Introduce your code (NIF without letter): ")
    profile = validate(user)

    if profile == 'admin':
        entrada = ''
        print("Welcome at the web service! These are the actions that you can do")

        while True:
            print("1 - GET EXAM")
            print("2 - ADD EXAM")
            print("3 - MODIFY ALL EXAM")
            print("4 - MODIFY DESCRIPTION")
            print("5 - DELETE EXAM")
            print("6 - ADD GRADES")
            print("7 - STORE EXAMS")
            print("8 - STORE GRADES")
            print("9 - EXIT PROGRAM")

            entrada = input("Which action do you want to do? ")

            if entrada == '1':
                get()
            elif entrada == '2':
                post()
            elif entrada == '3':
                mod()
            elif entrada == '4':
                modify_description()
            elif entrada == '5':
                delete()
            elif entrada == '6':
                add_grades()
            elif entrada == '7':
                save_exams()
            elif entrada == '8':
                save_grades()
            elif entrada == '9':
                sys.exit()
    else:
        student = input("Do you want to see your grades? (Y/N): ")

        if student == 'Y':
            resp = 'http://localhost:8080/Practica2Web/rest/storegrade'
            value = requests.post(url=resp, data=user)
            value = str(value.content)[2:-1]
            value = value.split('?')

            for i in value:
                print(i)
        else:
            sys.exit()
