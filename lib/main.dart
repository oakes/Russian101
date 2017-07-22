import 'dart:io';
import 'package:flutter/material.dart';
import 'package:audioplayer/audioplayer.dart';
import 'package:path_provider/path_provider.dart';

void main() {
  runApp(new Russian101());
}

var menuRows = [
  ["Alphabet", "алфавит", 35],
  ["Meeting People", "Знакомство", 9],
  ["Family", "семья", 8],
  ["Where do you work?", "Где вы работаете?", 13],
  ["Where do you live?", "Где вы живете?", 8],
  ["Shopping", "покупки", 27],
  ["In the restaurant", "В ресторане", 23],
  ["Transportation", "транспорт", 18],
  ["In the hotel", "В гостинице", 18],
  ["The telephone", "телефон", 24],
];

const listWidth = 300.0;

/*
 * Russian101
 */

class Russian101 extends StatelessWidget {
  Map<String, WidgetBuilder> createRoutes() {
    var routes = new Map<String, WidgetBuilder>();
    for (var i=0; i<menuRows.length; i++) {
      var row = menuRows[i];
      routes.putIfAbsent(
          row[0].toString(),
          () => (BuildContext context) {
            return new Scaffold(
                appBar: new AppBar(
                    title: new Text(row[0]),
                ),
                body: new Lesson(lessonNum: i+1, pageCount: row[2]),
            );
          }
      );
    }
    return routes;
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      theme: new ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: new Home(title: 'Russian 101'),
      routes: createRoutes(),
    );
  }
}

/*
 * Home
 */

class Home extends StatefulWidget {
  Home({Key key, this.title}) : super(key: key);

  final String title;

  @override
  HomeState createState() => new HomeState();
}

class HomeState extends State<Home> {
  int currentLesson;

  @override
  void initState() {
    super.initState();
    currentLesson = 1;
  }

  List<Widget> createRows(BuildContext context, bool isSmall) {
    var rows = new List<Widget>();
    for (var i=0; i<menuRows.length; i++) {
      var row = menuRows[i];
      rows.add(new ListTile(
        title: new Text(row[0]),
        subtitle: new Text(row[1]),
        selected: currentLesson == i+1,
        onTap: () {
          setState(() {
            currentLesson = i+1;
          });
          if (isSmall) {
            Navigator.of(context).pushNamed(row[0]);
          }
        },
      ));
    }
    return rows;
  }

  @override
  Widget build(BuildContext context) {
    return new LayoutBuilder(builder: (BuildContext context, BoxConstraints constraints) {
      if (constraints.maxWidth < listWidth * 2) {
        return new Scaffold(
          appBar: new AppBar(
            title: new Text(widget.title),
          ),
          body: new ListView(
            padding: new EdgeInsets.all(8.0),
            children: createRows(context, true),
          ),
        );
      }
      else {
        return new Scaffold(
          appBar: new AppBar(
            title: new Text(widget.title),
          ),
          body: new Row(
            mainAxisSize: MainAxisSize.max,
            children: [
              new Container(
                width: listWidth,
                child: new ListView(
                  padding: new EdgeInsets.all(8.0),
                  children: createRows(context, false),
                ),
              ),
              new Expanded(
                child: new Lesson(lessonNum: currentLesson, pageCount: menuRows[currentLesson-1][2]),
              ),
            ]
          ),
        );
      }
    });
  }
}

/*
 * Lesson
 */

createPageRoute (int lessonNum, int pageNum, int pageCount) {
  return new MaterialPageRoute<Null>(
      builder: (BuildContext context) {
        return new Page(lessonNum: lessonNum, pageNum: pageNum, pageCount: pageCount);
      }
  );
}

class Lesson extends StatelessWidget {
  Lesson({Key key, this.lessonNum, this.pageCount}) : super(key: key);

  final int lessonNum;
  final int pageCount;

  List<Widget> createPages(BuildContext context) {
    var pages = new List<Widget>();
    for (var i=1; i<=pageCount; i++) {
      pages.add(new GestureDetector(
        child: new Image(
          image: new AssetImage("assets/lesson$lessonNum/$i.png"),
          fit: BoxFit.contain,
        ),
        onTap: () {
          Navigator.of(context).push(createPageRoute(lessonNum, i, pageCount));
        }
      ));
    }
    return pages;
  }

  @override
  Widget build(BuildContext context) {
    return new GridView.extent(
      children: createPages(context),
      maxCrossAxisExtent: 150.0,
      mainAxisSpacing: 10.0,
      crossAxisSpacing: 10.0,
    );
  }
}

/*
 * Page
 */

class Page extends StatefulWidget {
  Page({Key key, this.lessonNum, this.pageNum, this.pageCount}) : super(key: key);

  final int lessonNum;
  final int pageNum;
  final int pageCount;

  @override
  PageState createState() => new PageState();
}

class PageState extends State<Page> {
  bool isPlaying = false;
  Duration position;
  Duration duration;
  AudioPlayer audioPlayer;

  play() async {
    final soundData = await DefaultAssetBundle.of(context).load("assets/lesson${widget.lessonNum}/${widget.pageNum}.m4a");
    final bytes = soundData.buffer.asUint8List();
    final dir = await getApplicationDocumentsDirectory();
    final file = new File("${dir.path}/${widget.lessonNum}-${widget.pageNum}.m4a");
    await file.writeAsBytes(bytes);
    final result = await audioPlayer.play(file.path, isLocal: true);
    if (result == 1) {
      setState(() {
        isPlaying = true;
      });
    }
  }

  pause() async {
    final result = await audioPlayer.pause();
    if (result == 1) {
      setState(() {
        isPlaying = false;
      });
    }
  }

  Widget createPlayer() {
    var playButton = new IconButton(
        onPressed: play,
        iconSize: 50.0,
        icon: new Icon(Icons.play_arrow),
        color: Colors.blue
    );
    var pauseButton = new IconButton(
        onPressed: pause,
        iconSize: 50.0,
        icon: new Icon(Icons.pause),
        color: Colors.blue
    );

    return new Material(
        child: new Row(
            mainAxisSize: MainAxisSize.max,
            children: [
            isPlaying ? pauseButton : playButton,
              new Expanded(
                child: new Slider(
                    value: position != null &&
                        position.inMilliseconds > 0
                        ? position.inMilliseconds /
                        duration.inMilliseconds
                        : 0.0,
                    onChanged: (newVal) {
                        if (duration != null) {
                          audioPlayer.seek(newVal * duration.inSeconds);
                        }
                    },
                )
              ),
            ]
        )
    );
  }

  @override
  void initState() {
    super.initState();
    audioPlayer = new AudioPlayer();
    audioPlayer.setDurationHandler((d) => setState(() {
      duration = d;
    }));
    audioPlayer.setPositionHandler((p) => setState(() {
      position = p;
    }));
    audioPlayer.setCompletionHandler(() {
      Navigator.of(context).pop();
      if (widget.pageNum < widget.pageCount) {
        Navigator.of(context).push(
          createPageRoute(widget.lessonNum, widget.pageNum+1, widget.pageCount)
        );
      }
    });
  }

  @override
  void dispose() {
    super.dispose();
    audioPlayer.stop();
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text("Page " + widget.pageNum.toString())
        ),
        body: new Column(
            mainAxisSize: MainAxisSize.max,
            children: [
              createPlayer(),
              new Image(
                image: new AssetImage("assets/lesson${widget.lessonNum}/${widget.pageNum}.png"),
                fit: BoxFit.contain,
              ),
            ]
        )
    );
  }
}