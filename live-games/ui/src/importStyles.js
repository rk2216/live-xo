const homeScreenStyles = require.context('./HomeScreen', true, /.scss$/);
homeScreenStyles.keys().forEach(homeScreenStyles);

const gameScreenStyles = require.context('./GameScreens', true, /.scss$/);
gameScreenStyles.keys().forEach(gameScreenStyles);