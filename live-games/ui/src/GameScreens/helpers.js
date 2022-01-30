export const computeRank = (userMap = {}, members = []) => {
    const sortedScores = [...new Set(Object.values(userMap).map(user => user.score))].sort().reverse();
    return members.map(member => {
        const score = userMap[member] && userMap[member].score;
        return sortedScores.indexOf(score) + 1;
    });
};