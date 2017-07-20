import 'package:flutter/material.dart';

void main() {
  runApp(new Russian101());
}

var menuRows = [
  ["Alphabet", "алфавит"],
  ["Meeting People", "Знакомство"],
  ["Family", "семья"],
  ["Where do you work?", "Где вы работаете?"],
  ["Where do you live?", "Где вы живете?"],
  ["Shopping", "покупки"],
  ["In the restaurant", "В ресторане"],
  ["Transportation", "транспорт"],
  ["In the hotel", "В гостинице"],
  ["The telephone", "телефон"],
];

class Russian101 extends StatelessWidget {
  Map<String, WidgetBuilder> createRoutes() {
    var routes = new Map<String, WidgetBuilder>();
    menuRows.forEach((row){
      routes.putIfAbsent(
          row[0].toString(),
          () => (BuildContext context) => new Lesson(title: row[0])
      );
    });
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

class Lesson extends StatefulWidget {
  Lesson({Key key, this.title}) : super(key: key);

  final String title;

  @override
  LessonState createState() => new LessonState();
}

class LessonState extends State<Lesson> {
  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text(widget.title),
      ),
    );
  }
}