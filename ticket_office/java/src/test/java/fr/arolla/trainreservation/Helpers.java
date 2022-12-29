package fr.arolla.trainreservation;

public class Helpers {
  public static Train makeEmptyTrain() {
    var train = new Train();
    for (var coach : new String[]{"A", "B", "C", "D", "E", "F"}) {
      for (var i = 0; i < 10; i++) {
        String number = Integer.toString(i);
        var seat = Seat.empty(number, coach);
        train.add(seat);
      }

    }
    return train;
  }
}
