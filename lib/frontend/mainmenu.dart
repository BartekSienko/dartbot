import 'package:flutter/material.dart';


class MainMenu extends StatefulWidget{
  const MainMenu({super.key});

  @override
  State<MainMenu> createState() => _MainMenuState();
}

class _MainMenuState extends State<MainMenu> {

  String playerName = "L. Humphries";
  double player3DA = 0.0;
  int playerScore = 501;
  int playerDartThrown = 0;



  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          backgroundColor: Colors.green,
          title: const Text('Dartbot Redux'),
          centerTitle: true,
        ),
        
        body: Column(
          children: [
            Row(
              children: [
                Text(playerName,
                    style: TextStyle(
                      fontSize: 20 
                    ),), 
                Text(player3DA.toString()), Text(playerScore.toString()), Text(playerDartThrown.toString())
              ],
            ),
          ],
        )
      )
    );
  }
}