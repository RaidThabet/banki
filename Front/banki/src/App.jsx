import Home from "./pages/Home";
import CheckBalance from "./pages/CheckBalance";
import BeneficiaryService from "./pages/BeneficiaryService";
import AccountService from "./pages/AccountService";
import TransactionHistoryService from "./pages/TransactionHistoryService";
import TransferService from "./pages/TransferService";
import { Routes, Route } from 'react-router-dom'; 

// Placeholder components (these will NOT have a Navbar unless you manually add it)
const Chat = () => <div className="p-10 pt-20"><h2>Chat Page (NO NAVBAR)</h2></div>;
const Report = () => <div className="p-10 pt-20"><h2>Report Page (NO NAVBAR)</h2></div>;

function App() {
  return (
    // We no longer use PageLayout or render Navbar globally
    <>
      <Routes>
        {/* Only the Home route will now contain the Navbar */}
        <Route path="/" element={<Home />} />
        
        {/* Other routes will be bare */}
        <Route path="/checkbalance" element={<CheckBalance />} />
        <Route path="/home" element={<Home />} />
        <Route path="/addBeneficiary" element={<BeneficiaryService />} />
        <Route path="/accountInfo" element={<AccountService />} />
        <Route path="/history" element={<TransactionHistoryService />} />
        <Route path="/transaction" element={<TransferService />} />
      </Routes>
    </>
  );
}

export default App;