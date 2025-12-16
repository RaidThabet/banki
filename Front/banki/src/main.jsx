import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import App from './App'
import './index.css'
import { CheckBalanceProvider } from "./contexts/CheckBalanceContext.jsx";
import { BeneficiaryProvider } from './contexts/BeneficiaryContext.jsx'
import { TransferProvider } from './contexts/TransferContext.jsx'
import { HistoryProvider } from './contexts/HistoryContext.jsx'
import { AccountProvider } from './contexts/AccountContext.jsx'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
    <AccountProvider>
    <HistoryProvider>
    <TransferProvider>
    <BeneficiaryProvider>
    <CheckBalanceProvider>
      <App />
    </CheckBalanceProvider>
    </BeneficiaryProvider>
    </TransferProvider>
    </HistoryProvider>
    </AccountProvider>
    </BrowserRouter>
  </React.StrictMode>
)