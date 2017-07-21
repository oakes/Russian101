import 'package:flutter/material.dart';

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

class Russian101 extends StatelessWidget {
  Map<String, WidgetBuilder> createRoutes() {
    var routes = new Map<String, WidgetBuilder>();
    for (var i=0; i<menuRows.length; i++) {
      var row = menuRows[i];
      routes.putIfAbsent(
          row[0].toString(),
          () => (BuildContext context) => new Lesson(title: row[0], lessonNum: i+1, pageCount: row[2])
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

class Home extends StatelessWidget {
  Home({Key key, this.title}) : super(key: key);

  final String title;

  List<Widget> createRows(BuildContext context) {
    var rows = new List<Widget>();
    menuRows.forEach((row) {
      rows.add(new ListTile(
        title: new Text(row[0]),
        subtitle: new Text(row[1]),
        onTap: () {
          Navigator.of(context).pushNamed(row[0]);
        },
      ));
    });
    return rows;
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(title),
      ),
      body: new ListView(
        padding: new EdgeInsets.all(8.0),
        children: createRows(context),
      ),
    );
  }
}

class Lesson extends StatelessWidget {
  Lesson({Key key, this.title, this.lessonNum, this.pageCount}) : super(key: key);

  final String title;
  final num lessonNum;
  final num pageCount;

  List<Widget> createPages(BuildContext context) {
    var pages = new List<Widget>();
    for (var i=1; i<=pageCount; i++) {
      pages.add(new Image(
        image: new AssetImage("assets/lesson$lessonNum/$i.png"),
        fit: BoxFit.contain,
      ));
    }
    return pages;
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(title),
      ),
      body: new GridView.extent(
        children: createPages(context),
        maxCrossAxisExtent: 150.0,
        mainAxisSpacing: 10.0,
        crossAxisSpacing: 10.0,
      )
    );
  }
}

class Page extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
      ),
    );
  }
}