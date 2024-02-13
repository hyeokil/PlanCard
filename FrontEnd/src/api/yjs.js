import * as Y from "yjs";

const doc = new Y.Doc(); // Yjs 배열 초기화

const yCardList = doc.getArray("cardList");
const yPlanList = doc.getArray("planList");

export { doc, yCardList, yPlanList };
