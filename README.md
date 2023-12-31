# Camelot
Описание классов:
  1) AudioModel:
Класс AudioModel представляет аудио-модель для каждого трека в плеере. Содержит информацию о пути к файлу, названии, продолжительности, исполнителе и дате добавления. Реализует интерфейс Parcelable для передачи объектов между компонентами Android.
  2) ButtonAnimator:
ButtonAnimator отвечает за анимацию кнопок в пользовательском интерфейсе. Позволяет анимировать нажатие кнопок, изменяя их масштаб с использованием ObjectAnimator.
  3) DateAddedComparator:
DateAddedComparator – компаратор для сортировки объектов AudioModel по дате добавления. В случае равенства дат используется сортировка по названию файла.
  4) MainActivity:
Основная активность приложения, где происходит инициализация интерфейса, загрузка списка песен из хранилища устройства, их отображение в RecyclerView, а также управление перемешиванием и сортировкой треков.
 
  5) MusicListAdapter:
Адаптер для RecyclerView, отображающий список песен в главной активности. Обновляет цвет текста активного трека и обрабатывает клики для перехода к активности проигрывания.
  6) MusicPlayerActivity:
Активность для воспроизведения музыки, отображает информацию о текущем треке, обеспечивает управление воспроизведением, а также отображает SeekBar для перемещения по треку.
  7) MyMediaPlayer:
Синглтон MyMediaPlayer предоставляет единственный экземпляр MediaPlayer для управления воспроизведением музыки в приложении. Содержит также статическую переменную currentIndex, отслеживающую текущий индекс проигрываемого трека.
