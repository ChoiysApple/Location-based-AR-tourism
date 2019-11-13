import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: Container(height: 0.0),
    );
  }
}
import 'package:flutter/material.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see the
        // application has a blue toolbar. Then, without quitting the app, try
        // changing the primarySwatch below to Colors.green and then invoke
        // "hot reload" (press "r" in the console where you ran "flutter run",
        // or simply save your changes to "hot reload" in a Flutter IDE).
        // Notice that the counter didn't reset back to zero; the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: Container(height: 0.0),
    );
  }
}

Widget build(BuildContext context) {
  return Scaffold(
    body: SafeArea(
      child: ListView(
        padding: EdgeInsets.symmetric(horizontal: 24.0),
        children: <Widget>[
          SizedBox(height: 80.0),
          Column(
            children: <Widget>[
              Image.asset('assets/diamond.png'),
              SizedBox(height: 16.0),
              Text('SHRINE'),
            ],
          ),
          SizedBox(height: 120.0),
          // TODO: Wrap Username with AccentColorOverride (103)
          // TODO: Remove filled: true values (103)
          // TODO: Wrap Password with AccentColorOverride (103)
          // TODO: Add TextField widgets (101)

          // [Name]
          AccentColorOverride(
            color: Colors.grey[50],
            child: TextField(
              decoration: InputDecoration(
                labelText: 'Username',
              ),
            ),
          ),
          // spacer
          SizedBox(height: 12.0),
          // [Password]

          AccentColorOverride(
            color: Colors.grey[50],
            child: TextField(
              decoration: InputDecoration(
                labelText: 'Password',
              ),
            ),
          ),
          // TODO: Add button bar (101)
          ButtonBar(
            children: <Widget>[
              // TODO: Add buttons (101)
              FlatButton(
                child: Text('cancel'),
                shape: BeveledRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(7.0)),
                ),
                onPressed: () {
                  // TODO: Clear the text fields (101)

                },
              ),
              // TODO: Add an elevation to NEXT (103)
              // TODO: Add a beveled rectangular border to NEXT (103)
              RaisedButton(
                child: Text('Next'),
                shape: BeveledRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(7.0)),
                ),
                elevation: 0.0,
                onPressed: () {
                  // TODO: Show the next page (101)
                  Navigator.pop(context);
                },
              ),
            ],
          )
        ],
      ),
    ),
  );
}


// TODO: Add AccentColorOverride (103)

class AccentColorOverride extends StatelessWidget{
  const AccentColorOverride({Key key, this.color, this.child})
      :super(key: key);

  final Color color;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Theme(
      child: child,
      data: Theme.of(context).copyWith(
        accentColor: color,
        brightness: Brightness.dark,
      ),
    );
  }


}}