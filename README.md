# KursJavaWino
Определение функциональных требований:
Данные, хранящиеся в приложении, будут связаны с вином. Храниться всё будет в файлах txt формата. 
Нужно будет сделать справочник вин. Данные - Идентификатор вина, Бренд вина, Тип вина, Сорт вина, Происхождение вина, Дата розлива, Содержание алкоголя, Индекс поставщика, Цена на закупку, Цена на продажу. (Цена не может быть отрицательной)
Необходим файл справочник с поставщиками. Данные – Индекс поставщика, ИНН поставщика, Наименование поставщика (Могут быть как ИП так и другие юридические компании по типу ООО и другие.), Адрес.
Необходим файл справочник с покупателями. Данные – Индекс покупателя, ИНН покупателя, Наименование поставщика (Могут быть как ИП так и другие юридические компании по типу ООО и другие.), Адрес.
Для дальнейшего упрощения работы нужен справочник наших складов. Данные – Адрес.
Также необходимо создать файлы нескольких складов (наименование файла будет равно адресу склада), где осуществляется хранение и учёт уже приобретённого вина. Данные - Индекс вина, количество бутылок на этом складе. 
Нужен будет файл учёта заявок на закупку вина, где будут отображены все необработанные заявки. Он будет пополняться и чиститься по мере составления заявок и их обработке соответственно. Данные – Индекс покупки, Индекс поставщика, Перечень индексов вин (возможно добавление только тех вин, которые указаны в доступных от этого поставщика), перечень количества бутылок связанных соответственно с индексом вин, Адрес доставки, Суммарная стоимость при покупке.
Нужен будет файл учёта заявок на покупку нашего вина, где будут отображены все необработанные заявки. Он будет пополняться и чиститься по мере прихода заявок и их обработке соответственно. Данные – Индекс продажи, Индекс покупателя, Перечень индексов вин, перечень количества бутылок связанных соответственно с индексом вин, Адрес склада, Адрес доставки, Суммарная стоимость при продаже.
Для отображения деятельности организации необходимо добавить два файла учёта совершённых покупок и продаж соответственно. Файл совершённых покупок будет пополняться после подтверждения прибытия товара на наш склад. Файл совершённых продаж будет пополняться после подтверждения прибытия товара на склад покупателя. Данные – Индекс продажи или покупки, Дата совершения операции, Индекс покупателя или поставщика, Перечень индексов вин, перечень количества бутылок связанных соответственно с индексом вин, Адрес доставки, Суммарная стоимость при продаже или покупки.
Какие операции должны быть доступны:
Добавление, удаление, редактирование, поиск, фильтрация по всем доступным файлам. 
Нужна ли работа с базой данных или достаточно хранения данных в памяти? Как было описано ранее данные будут храниться в файлах txt формата и соответственно работать будем именно через них.
Какие отчеты или статистика должны быть доступны? Можно будет создать методы отображения отчётов по проведённым операциям за период, отчёт по наличию на складах. Можно будет составить отчёт-статистику по продаже определённых вин.
Проектирование интерфейса: 
Какие окна и элементы управления будут в программе?
Визуальная составляющая исходного окна будет следующей:
Перечень доступных операций: Закупка, Продажа, Подтверждение совершения операции, Отчёты, Перечень доступных данных. Это всё кнопки перехода к следующему окну.
Закупка:
Здесь будет совершаться составление заявки на покупку, можно будет ввести поставщика из доступных, перечень вин которые поставляет этот поставщик, количество бутылок, склад куда будет направленна доставка из перечня доступных. Здесь будет отображаться суммарная стоимость отдельно для каждого вида вин и внизу справа будет отображаться итоговая стоимость закупки. Не забывай, что стоимость для закупок и продаж разная.
Продажа:
Здесь будет совершаться составление заявки на продажу, можно будет ввести покупателя из перечня доступных, перечень вин, количество бутылок, адрес доставки. Здесь будет отображаться суммарная стоимость отдельно для каждого вида вин и внизу справа будет отображаться итоговая стоимость закупки. Не забывай, что стоимость для закупок и продаж разная.
Подтверждение совершения операции:
После наития на кнопку всплывёт диалоговое окно для выбора типа операции – Подтверждение совершения покупки или Подтверждение совершения продажи. После нажатия на один из вариантов будет открываться окно с отображением всех незавершённых операций по типу, выбранном ранее. Здесь можно будет выбрать одну из незавершённых операций и нажать на кнопку Завершить. После этого операция будет удалена из файла учёта незавершённых операций и добавлена в файл учёта совершённых покупок или продаж (выбор опять же зависит от того, что мы выбрали ранее). Также в зависимости от типа операции в файле склада, с которого была отгрузка товара или на который прибыла поставка товара, будет изменено количество вина. В случае если была поставка вина, то количество вина прибавилось. Если пришла поставка вина, которого ранее не было, то оно добавляется на склад. В случае если была продажа вина, то количество вина убавилось. Если пытаться завершить продажу вина, которого нахватает на складе или его нет вообще, то выдать ошибку о том, что на складе нахватает вина или его нет.
Отчёты:
После перехода по кнопке отчёты будет отображено окно, на котором можно будет выбрать тип отчёта: Отобразить количество проданных вин за период, Отобразить статистику продажи определённого вина, Отобразить наличие товара на определённом складе, Поиск определённого вина по всем складам.
Перечень доступных данных:
Здесь можно будет прочитать, редактировать, добавлять и удалять данные из справочников. Изначально будут кнопки открытия определённых справочников: Справочник вин, Поставщики, Покупатели, Адреса складов. После выбора одного из справочников, будет открыт перечень данных, находящихся в этом справочнике. В нём можно будет изменить данные, добавить или удалить их.
Комментарии и предложения:
Справочники:
Добавьте поле "Телефон" для удобства связи.
Файлы учёта заявок:
Операции:
Для фильтрации можно добавить возможность сортировки по разным полям (например, по цене, дате, количеству).
Отчёты:
Для отчёта "Количество проданных вин за период" добавь возможность выбора периода (начальная и конечная дата).
Для отчёта "Статистика продажи определённого вина" можно добавить график (например, с использованием библиотеки JFreeChart).
Интерфейс:
Главное окно: Всё логично. Можно добавить иконки для кнопок, чтобы интерфейс был более дружелюбным.
Окно закупки и продажи:
Для удобства можно добавить автозаполнение полей (например, при выборе поставщика автоматически подгружать список доступных вин).
Окно подтверждения операций:
Добавь возможность отмены операции (например, если заявка была создана по ошибке).
Можно добавить уведомление о недостатке товара на складе перед завершением продажи.
Окно справочников:
Добавь возможность массового редактирования (например, изменение цены для нескольких вин одновременно).
7. Дополнительные функции:
Модульность программы:
Раздели программу на модули (например, отдельный модуль для работы с файлами, отдельный для интерфейса). Это упростит поддержку и расширение функциональности.
Тестирование:
Напиши unit-тесты для критически важных функций (например, добавление/удаление данных, расчет стоимости).
